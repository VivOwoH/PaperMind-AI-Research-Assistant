# Wed-11-02-Lab-Group-6
Research Assistant

## How to start the project

### Database Prerequisites
- Ensure you have postgresql installed with the correct database and user credential configuration
- Ensure the postgresql server is running on your device

### SpringBoot Application
- To build the SpringBoot application navigate to the project directory and run ```./gradlew build```
- Run the app with```./gradlew bootRun```

### React framework
- Ensure you have Node.js installed
- In another terminal window, navigate to ```frontend``` directory within ```main/main``` and run ```npm start``` to start your React application
- View the frontend on the browser at ```http://localhost:3000```

### API key
- Obtain your own API key from https://aistudio.google.com/app/apikey
- Navigate to application.properties, and add this line ```apiKey={yourKey}```

## How to run tests
Run the following command to run the tests and build the reports.
```./gradlew clean test jacocoTestReport```
Find the jacoco test report in the directory build/jacocoHtml
