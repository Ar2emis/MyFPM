package com.example.myfpm

import android.os.StrictMode
import java.sql.Connection
import java.sql.DriverManager

class ConnectionClass {

    private val ip = "ssprogram.database.windows.net:1433"
    private val db = "Users"
    private val username = "Ar2emis@ssprogram"
    private val  password = "Minecraft125"

    fun Connect() : Connection? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val connString = """jdbc:jtds:sqlserver://$ip;database=$db;"""
        //"user=$username;password=$password;"""
        //"encrypt=true;trustServerCertificate=false;
        //"hostNameInCertificate=*.database.windows.net;loginTimeout=30;"""

        Class.forName("net.sourceforge.jtds.jdbc.Driver")

        return DriverManager.getConnection(connString, username, password)
    }

}