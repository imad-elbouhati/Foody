# Foody

# MVVM Architecture
This project follows the Model-View-ViewModel (MVVM) architecture pattern. The MVVM pattern separates the user interface (View) from the data (Model) and the logic (ViewModel) of an application.

The Model represents the data and business logic of the application, the View displays the user interface, and the ViewModel acts as a mediator between the View and the Model, providing data to the View and updating the Model as necessary.

By using the MVVM pattern, the code is more modular, easier to maintain, and testable.

# Continuous Integration and Continuous Deployment (CI/CD)
This project uses GitHub as a platform for Continuous Integration and Continuous Deployment (CI/CD).

**Continuous Integration (CI)** is the process of automatically building and testing the code whenever a change is made. This helps to catch errors and bugs early in the development process.

**Continuous Deployment (CD)** is the process of automatically deploying the code to a staging or production environment whenever a change is made and successfully passed all tests.

**In addition, the project utilizes the following tools to facilitate the CI/CD process:**

# GitHub
**GitHub** is used as a version control system and as a platform for CI/CD. By integrating GitHub with other tools, such as Fastlane and Firebase Distribution, the project can automate the build, testing, and deployment processes.

# Fastlane
Fastlane is an open-source platform that simplifies the deployment of Android and iOS applications. It automates the process of building, testing, and deploying applications, saving time and reducing errors.

# Firebase Distribution
**Firebase Distribution** is a tool provided by Google Firebase that facilitates the distribution of applications to trusted testers. By quickly installing the application on the devices of testers, feedback can be obtained early and often, helping to identify and resolve issues quickly.

Overall, the use of these tools in conjunction with GitHub allows for a streamlined and efficient CI/CD process, enabling the project team to focus on developing high-quality code.


<figure>
  <img src="https://i.imgur.com/Y5YfTuR.png" alt="alt text">
  <figcaption>CI/CD Architecture</figcaption>
</figure>
