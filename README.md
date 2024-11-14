

🎮 Cafe PlayStation Management System
=====================================
<p align="center">
  <img src="https://github.com/user-attachments/assets/95f13973-b253-48bb-8273-be78191ee652" width="100" />
</p>

# VarPsManager
Welcome to the **Cafe PlayStation Management System**! This application is built to streamline management of PlayStation devices, tables, orders, and user shifts in a cafe environment. With an interactive admin panel, you can easily monitor real-time pricing, manage orders, and handle device allocation.

<p align="center">
  <img src="https://github.com/user-attachments/assets/ad31dd7f-f8b0-405f-ab98-23f6c8ad6d4f" width="800" />
</p>


<p align="center">
  <img src="https://github.com/user-attachments/assets/1a72dd52-fe16-4f4e-a5a8-2f967a76ec6c" width="800" />
</p>


<p align="center">
  <img src="https://github.com/user-attachments/assets/de456a0e-5a8a-4088-8140-363dc20f6bc2" width="800" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/af24a598-b974-4141-b884-006f6faffd42" width="800" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/fc8a1784-8cb1-4454-9fd2-6d7c1c8121c7" width="800" />
</p>










🚀 Features
-----------

### 🎲 Device & Table Management

*   **Manage PlayStation Devices**: Track device status, control start/stop times, and assign custom prices for each device type.
    
*   **Table Management**: Organize and assign orders to cafe tables, making it simple to coordinate seating and device usage.
    

### 🧾 Orders & Billing

*   **Live Order Management**: Update orders in real-time with a live price calculator based on the device type and usage duration.
    
*   **Print Receipts**: Quickly print bills and order summaries for customers.
    

### 👤 Admin Panel

*   **User Roles & Permissions**: Centralized admin panel for managing staff shifts and permissions.
    
*   **Device Types & Pricing**: Configure custom pricing for each device type (e.g., PlayStation 4, PlayStation 5, VR setups) directly from the admin panel.
    

### ⏰ Shift Management

*   **User Shift Tracking**: Seamlessly track shift times and changes, allowing for smooth transitions between users.
    
*   **Admin Shift Control**: Ensure accountability with shift start and end time logs for each user.
    

🖥️ Tech Stack
--------------

*   **Frontend**: Angular for a responsive UI and live updates.
    
*   **Backend**: Spring Boot to handle device and order data, and real-time pricing updates.
    
*   **Database**: MongoDB for scalable and flexible data storage for orders, devices, and shifts.
    
*   **Printing**: Integrated print support for instant billing.
    

📜 Setup Guide
--------------

### Prerequisites

*   **Node.js** and **Angular CLI** for frontend development.
    
*   **Java (JDK 11+)** for the Spring Boot backend.
    
*   **MongoDB** for document-based database management.
    

### Installation

1.  git clone [https://github.com/adelelawady/VarPlaystationManager)](https://github.com/adelelawady/VarPlaystationManager.git)
    
2.  **Start Server and Ui**

    ```
    cd VarPlaystationManager
    ./mvnw
    ```
        
3.  **Access Application**
    
    *   Open your browser and navigate to http://localhost:4200 for the frontend.
        
    *   Access the API documentation at http://localhost:8080/swagger-ui for backend details.
        

### ⚙️ Initial Setup

*   **Add Devices, Device Types, and Orders**: After installing, go to the admin panel to add your initial devices and define device types with custom prices. Set up at least one order to begin using the system efficiently.
    

🛠️ Usage
---------

### 📅 Managing Devices

1.  Go to the Devices tab to assign or release devices for customer use.
    
2.  Set a custom price per hour for each device based on type.
    

### 💰 Order Processing

1.  Create a new order, assign it to a table, and add selected devices.
    
2.  Adjust device times in real-time; the app calculates pricing based on duration and device type.
    

### 🔄 Shift Management

1.  View current shifts and log shift start/end times for each user.
    
2.  Admins can monitor shifts, ensuring smooth user transitions.
    

### STARTING DEVICE 
    DOUBLE CLICK TO START DEVICE TIME


📄 License
----------

This project is licensed under the MIT License - see the LICENSE file for details.


DEVELOPMENT
-----------






This application was generated using JHipster 7.3.1, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.3.1](https://www.jhipster.tech/documentation-archive/v7.3.1).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is disabled by default. To enable it, uncomment the following code in `src/main/webapp/app/app.module.ts`:

```typescript
ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
```

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

### JHipster Control Center

JHipster Control Center can help you manage and control your application(s). You can start a local control center server (accessible on http://localhost:7419) with:

```
docker-compose -f src/main/docker/jhipster-control-center.yml up
```

## Building for production

### Packaging as jar

To build the final jar and optimize the er application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a mongodb database in a docker container, run:

```
docker-compose -f src/main/docker/mongodb.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/mongodb.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.3.1 archive]: https://www.jhipster.tech/documentation-archive/v7.3.1
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.3.1/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.3.1/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.3.1/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.3.1/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.3.1/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.3.1/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[webpack]: https://webpack.github.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
[angular cli]: https://cli.angular.io/
