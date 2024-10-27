# Wed-11-02-Lab-Group-6

**PaperMind - AI powered Research Assistant**

PaperMind provides an easy and accessible way to search for key research topics, ideas or opinions and retrieve a list or network graph visualisation of the related studies, alongside summarised content of the studies. Users will get the option between two ways of connecting the research papers they prompt for: the first is based on citation metrics for connected papers and the second is based upon the supporting and opposing opinions for the input prompt. The output view will provide users a list or graph perspective, for easy and identifiable visual connections.

## Features

- **Filter and Search**: Effortlessly search through thousands of research articles using the Semantic Scholar API. Apply filters by publication year, author, and type for more targeted results. 
- **Citation Graphs**: Visualize research papers and their citation relationships in an intuitive, interactive graph, making it easy to explore connections.
- **Opinion Graphs**: Search and visualize research papers categorized by their stance on the main topic, displaying supporting and opposing opinions in an intuitive graph format.
- **AI-Powered Summaries**: Generate concise, easy-to-understand summaries for research papers, minimizing technical jargon.
- **Google Drive Integration**: Edit and Save AI-generated summaries directly in Google Drive in .docx format
- **Copy to Clipboard**: Quickly copy paper summaries for easy sharing and documentation.

## Quick start

### Prerequisites

#### Database (PostgreSQL)
- Ensure you have postgresql(14.3)installed with the correct database and user credential configuration
- Ensure the postgresql server is running on your device

#### SpringBoot Application
- Ensure you have Java(Java 17) installed

#### React framework
- Ensure you have Node.js(v20.10.0) installed

#### API keys
- **Google AI Studio API** 
    - Obtain your own API key from https://aistudio.google.com/app/apikey
    - Navigate to application-dev.properties, and add this line ```apiKey={yourKey}```

- **Google Drive API**
    - Obtain your API key after creating a project in Google Cloud Console (https://console.cloud.google.com/) and setting up with your project details
        - User type set to External in Google OAuth 2.0 setup 
        - Choose web application for application type
        - Example name for OAuth  Client - "React App"
        - Authorised redirected URLs - `http://localhost:3000`
    - In the root of React Project (inside ```frontend```directory), create a file named ```.env```
    - Add the following 2 lines,
        ```REACT_APP_CLIENT_ID={yourClientId}``` and ```REACT_APP_API_KEY={yourApiKey}```

### Steps to Run the Project

#### 1. SpringBoot Application 
- To build the SpringBoot application navigate to the project directory which includes the build.gradle file and run ```./gradlew build```
- Run the app with```./gradlew bootRun```
- By default backend server will be available at ```http://localhost:8080```

#### 2. React Frontend
- In another terminal window, navigate to ```frontend``` directory within ```main/main``` 
- Install dependencies using ```npm install``` (first time setup)
- Execute ```npm start``` to start your React application
- View the frontend on the browser at ```http://localhost:3000```

### How to run tests
- Run the following command from the folder which contains the build.gradle to run the unit tests and build the reports.
```./gradlew clean test jacocoTestReport```
- Find the jacoco test report in the directory build/jacocoHtml

#### Contributors

- Tisha Jhabak      tjha8527@uni.sydney.edu.au      
- Angela Lai        alai7251@uni.sydney.edu.au
- Vivian Ha         weha7612@uni.sydney.edu.au
- Amanda Walpitage  awal8482@uni.sydney.edu.au          
