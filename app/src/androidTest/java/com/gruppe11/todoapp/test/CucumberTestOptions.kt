package com.gruppe11.todoapp.test

import dagger.hilt.android.testing.HiltAndroidTest
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["features"],
    glue = ["com.gruppe11.todoapp"]
)
class CucumberTestOptions