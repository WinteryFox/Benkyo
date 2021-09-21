package com.benkyo.accounts.controller

import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Controller

@Controller
class Controller(
    val database: DatabaseClient
) {

}