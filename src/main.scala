import java.beans.Statement
import java.sql.{Connection, DriverManager, SQLException}
import scala.io.StdIn


object main extends App {

    val bankuser = "admin"
    val bankpass = "pass"

    var inuser = ""
    var inpass = ""
    var admincredential = false

    do{
        print("Enter your username: ")
        inuser = StdIn.readLine()
        print("Enter your password: ")
        inpass = StdIn.readLine()
        println()

        if(inuser == bankuser && inpass == bankpass){
            println("Credentials Are: " + Console.GREEN + "VALID\n" + Console.RESET + "Welcome: " + inuser)
            admincredential = true
        }
        else{
            println("Credentials Are: " + Console.RED + "INVALID\n" + Console.RESET + "TRY AGAIN")
        }

    }while(!admincredential)


//    var connection:Connection = null

//    val isconnected = connecttodatabase()


//    if (isconnected) {

        mainmenu();




//    }



//ALL FUNCTIONS ARE BELOW THIS LINE//
    def mainmenu(): Unit = {

        var done = false;

        //Welcome message
        println(Console.CYAN + "========================")
        println("  Welcome to Arch Bank  ")
        println("========================" + Console.RESET)

        println("What would you like to do today?")
        println("Tip: Use your numerical pad to choose an option from the main menu")

        do {
            println(Console.BLUE + "===========")
            println(" Main Menu ")
            println("===========" + Console.RESET)

            println("1) Bank Menu")
            println("2) Exit")
            print("> ")
            var menuinput1 = StdIn.readLine().toString;

            //match the user input with a case
            menuinput1 match {

                case "1" => bankmenu()
                case "2" => done = true
                case _ => println(Console.RED + "ERROR, UNEXPECTED COMMAND: select a valid command from the selection menu" + Console.RESET)

            }


        } while (!done)


        println("=======================================\n" +
          "Thank you for using Arch Bank, Goodbye!\n" +
          "=======================================")
    }


    def bankmenu(): Unit = {

        var done = false

        do{
            println(Console.BLUE + "===========")
            println(" Bank Menu ")
            println("===========" + Console.RESET)

            println("1) See all clients")
            println("2) See all accounts")
            println("3) See all joint accounts")
            println("4) See all loans")
            println("5) Search for a client")
            println("6) Search for an account")
            println("7) Search for a joint account")
            println("8) Search for a loan")
            println("9) Create new client")
            println("10) Create new account")
            println("11) Create a new joint account")
            println("12) Take out a loan")
            println("13) Back")
            print("> ")
            var bankmenuinput = StdIn.readLine()

            bankmenuinput match {
                case "1" => readallquery("clients")
                case "2" => readallquery("accounts")
                case "3" => readallquery("jointaccounts")
                case "4" => readallquery("loans")
                case "5" => searchquery("clients")
                case "6" => searchquery("accounts")
                case "7" => searchquery("jointaccounts")
                case "8" => searchquery("loans")
                case "9" => writequery("clients")
                case "10" => writequery("accounts")
                case "11" => writequery("jointaccounts")
                case "12" => writequery("loans")
                case "13" => done = true
                case _ => println(Console.RED + "ERROR, UNEXPECTED COMMAND: select a valid command from the selection menu" + Console.RESET)
            }

        }while(!done)

    }


    //CREATES A NEW ENTRY IN A TABLE CHOSEN BY THE USER//
    def writequery(param:String): Unit ={

        //CONNECT TO DATABASE//
        val driver = "com.mysql.cj.jdbc.Driver"
        val url = System.getenv("JDBC_URL")
        val username = System.getenv("JDBC_USER")
        val password = System.getenv("JDBC_PASSWORD")

        var connection: Connection = DriverManager.getConnection(url, username, password)

        val statement = connection.createStatement()

        if(param == "clients"){
            var clientid = generateID(param,"clientid",1)

            print("What is your first name? ")
            var firstname = StdIn.readLine()
            print("What is your last name? ")
            var lastname = StdIn.readLine()
            print("How old are you? ")
            var age = StdIn.readLine()
            print("What is your address? ")
            var address = StdIn.readLine()
            print("What city you live in? ")
            var city = StdIn.readLine()
            print("What country do you live in? ")
            var country = StdIn.readLine()

            statement.executeUpdate("INSERT INTO Clients (ClientID,FirstName,LastName,Age,Address,City,Country) \n" +
              "VALUES (" + clientid + ",'" + firstname + "','" + lastname + "'," + age + ",'" + address + "','" + city + "','" + country + "');")
        }
        else if(param == "accounts"){
            var accountid = generateID(param,"accountid",1)

            print("What is your Client ID? ")
            var clientid = StdIn.readLine()
            print("Would you like a Savings or Checking account? ")
            var accounttype = StdIn.readLine()
            print("How much do you like to deposit right now? $")
            var balance = StdIn.readLine().toDouble

            statement.executeUpdate("INSERT INTO Accounts (ClientID,AccountID,AccountType,Balance) \n" +
              "VALUES (" + clientid + "," + accountid + ",'" + accounttype + "'," + balance + ");")

        }
        else if(param == "jointaccounts"){
            print("What is your Account ID? ")
            var accountid = StdIn.readLine()
            print("What is the Client ID of the main owner of this account? ")
            var primaryaccountholderid = StdIn.readLine()
            print("What is the Client ID of the person you are trying to join to this account? ")
            var secondaryaccountholderid = StdIn.readLine()

            statement.executeUpdate("INSERT INTO JointAccounts (AccountID,PrimaryAccountHolderID,SecondaryAccountHolderID) \n" +
              "VALUES (" + accountid + "," + primaryaccountholderid + "," + secondaryaccountholderid + ");")

        }
        else if(param == "loans"){
            print("What is your Client ID? ")
            var clientid = StdIn.readLine()
            print("How much money are you trying to loan? ")
            var loanbalance =  StdIn.readLine().toDouble
            println("Your loan is $" + loanbalance + ", at a 3.5% APR. After APR, you will have to pay $" +
              (loanbalance + (loanbalance*.035)) + " by the end of your loan term." )
            loanbalance = loanbalance + (loanbalance*.035)

            statement.executeUpdate("INSERT INTO loans (ClientID,LoanBalance,APR) \n" +
              "VALUES (" + clientid + "," + loanbalance + ",3.5);")
        }

    }

    def generateID(table:String,column:String, id:Int): Int ={

        var newid = id;

        val driver = "com.mysql.cj.jdbc.Driver"
        val url = System.getenv("JDBC_URL")
        val username = System.getenv("JDBC_USER")
        val password = System.getenv("JDBC_PASSWORD")

        var connection: Connection = DriverManager.getConnection(url, username, password)

        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT Count(" + column + ") FROM " + table + " WHERE " + column + " = " + id + ";")

        if(resultSet.next() == false){
            return newid
        }
        else{

            var count = resultSet.getString(1).toInt

            if(count == 0){
                return newid
            }
            else{
                newid = generateID(table, column, newid + 1)
                return newid
            }
        }

    }


    //SEARCHES CLIENTS INFO,ACCOUNTS, JOINT ACCOUNTS, OR LOANS WITH A CLIENT ID//
    def searchquery(param:String): Unit =  {

        println("Enter the Client ID")
        print("> ")
        var searchqueryinput = StdIn.readLine()

        //CONNECT TO DATABASE//
        val driver = "com.mysql.cj.jdbc.Driver"
        val url = System.getenv("JDBC_URL")
        val username = System.getenv("JDBC_USER")
        val password = System.getenv("JDBC_PASSWORD")

        var connection: Connection = DriverManager.getConnection(url, username, password)
        var sql = "query statement goes here"

        if(param != "jointaccounts"){
            sql = "SELECT * FROM " + param + " WHERE clientid = " + searchqueryinput + ";"
        }
        else{
            sql = "SELECT * FROM " + param + " WHERE primaryaccountholderid = " + searchqueryinput + " OR " +
              "secondaryaccountholderid = " + searchqueryinput + ";"
        }

        val statement = connection.createStatement()
        val resultSet = statement.executeQuery(sql)


        if(param == "clients") {
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var clientid = resultSet.getString(1)
                    var firstname = resultSet.getString(2)
                    var lastname = resultSet.getString(3)
                    var age = resultSet.getString(4)
                    var address = resultSet.getString(5)
                    var city = resultSet.getString(6)
                    var country = resultSet.getString(7)

                    println("Client ID: " + clientid + " | First Name: " + firstname + " | Last Name: " + lastname
                      + " | Age: " + age + " | Address: " + address + " | City: " + city + " | Country: " + country)

                } while (resultSet.next())

            }

        }
        else if(param == "accounts"){
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var clientid = resultSet.getString(1)
                    var accountid = resultSet.getString(2)
                    var accounttype = resultSet.getString(3)
                    var balance = resultSet.getString(4)

                    println("Client ID: " + clientid + " | Account ID: " + accountid + " | Account Type: " + accounttype
                      + " | Balance: $" + balance)

                } while (resultSet.next())

            }

        }
        else if(param == "jointaccounts"){
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var accountid = resultSet.getString(1)
                    var primaryaccountholderid = resultSet.getString(2)
                    var secondaryaccountholderid = resultSet.getString(3)

                    println("Account ID: " + accountid + " | Primary Account Holder ID: " + primaryaccountholderid
                      + " | Secondary Account Holder ID: " + secondaryaccountholderid)

                } while (resultSet.next())

            }

        }
        else if(param == "loans"){
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var clientid = resultSet.getString(1)
                    var loanbalance = resultSet.getString(2)
                    var apr = resultSet.getString(3)

                    println("Client ID: " + clientid + " | Loan Balance: " + loanbalance + " | APR: " + apr + "%")

                } while (resultSet.next())

            }

        }


    }


    //READS ALL THE ENTRIES IN A GIVEN TABLE//
    def readallquery(param:String): Unit = {

        //CONNECT TO DATABASE//
        val driver = "com.mysql.cj.jdbc.Driver"
        val url = System.getenv("JDBC_URL")
        val username = System.getenv("JDBC_USER")
        val password = System.getenv("JDBC_PASSWORD")

        var connection: Connection = DriverManager.getConnection(url, username, password)

        val statement = connection.createStatement()
        //SELECTS ALL OF THE ENTRIES IN THE 'param' TABLE
        val resultSet = statement.executeQuery("SELECT * FROM " + param + ";")


        if(param == "clients") {
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var clientid = resultSet.getString(1)
                    var firstname = resultSet.getString(2)
                    var lastname = resultSet.getString(3)
                    var age = resultSet.getString(4)
                    var address = resultSet.getString(5)
                    var city = resultSet.getString(6)
                    var country = resultSet.getString(7)

                    println("Client ID: " + clientid + " | First Name: " + firstname + " | Last Name: " + lastname
                      + " | Age: " + age + " | Address: " + address + " | City: " + city + " | Country: " + country)

                } while (resultSet.next())

            }
        }
        else if(param == "accounts"){
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var clientid = resultSet.getString(1)
                    var accountid = resultSet.getString(2)
                    var accounttype = resultSet.getString(3)
                    var balance = resultSet.getString(4)

                    println("Client ID: " + clientid + " | Account ID: " + accountid + " | Account Type: " + accounttype
                      + " | Balance: $" + balance)

                } while (resultSet.next())

            }
        }
        else if(param == "jointaccounts"){
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var accountid = resultSet.getString(1)
                    var primaryaccountholderid = resultSet.getString(2)
                    var secondaryaccountholderid = resultSet.getString(3)

                    println("Account ID: " + accountid + " | Primary Account Holder ID: " + primaryaccountholderid
                      + " | Secondary Account Holder ID: " + secondaryaccountholderid)

                } while (resultSet.next())

            }
        }
        else if(param == "loans"){
            //IF THE TABLE IS NOT EMPTY, PRINT IT'S CONTENTS//
            if(resultSet.next() == false){
                println(Console.YELLOW + "No results found for this query" + Console.RESET)
            }
            else {
                do {
                    var clientid = resultSet.getString(1)
                    var loanbalance = resultSet.getString(2)
                    var apr = resultSet.getString(3)

                    println("Client ID: " + clientid + " | Loan Balance: " + loanbalance + " | APR: " + apr + "%")

                } while (resultSet.next())

            }
        }


    }


    def connecttodatabase(): Boolean = {

        try {

            val driver = "com.mysql.cj.jdbc.Driver"
            val url = System.getenv("JDBC_URL")
            val username = System.getenv("JDBC_USER")
            val password = System.getenv("JDBC_PASSWORD")

            var connection: Connection = DriverManager.getConnection(url, username, password)

            println("Connection: " + Console.GREEN + "Successful")
            return true;

        }
        catch {
            case s: SQLException => println("Connection: " + Console.RED + "Failure\n"
              + "SQLException Caught, unable to connect to the database" + Console.RESET)
                return false
        }

    }


}
