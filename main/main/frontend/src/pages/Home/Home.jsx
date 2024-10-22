import React from 'react';
import '../../styles/pages/Home/Home.css';
import '../../components/SearchComponent';
import SearchComponent from '../../components/SearchComponent';
import { SearchProvider } from '../../context/SearchContext';
import FeatureCarousel from '../../components/FeatureCarousel';
import HomeImage from '../../resources/HomeImage.jpeg';
import YellowHome from '../../resources/YellowHome.jpg';
import HImage from '../../resources/HImage.jpeg';
import { Card, CardContent, Typography, Box } from '@mui/material';

function Home() {

    const featureData = [
        { title: "Search By Any Topic", description: "Search any topic you want to learn more about. We access Semantic Scholar API for thousands of up-to-date research articles" },
        { title: "View Connected Papers", description: "View Graphs based on common citations and common opinions: supporting, opposing" },
        { title: "Generate Summaries", description: "Generate summaries from a paper abstract. The AI summaries generated will have minimal technical jargon" },
        { title: "Search By Citations", description: "Search for commonly cited papers based on ideas. All or Supporting or Opposing Ideas" },
        { title: "Search By Opinions", description: "Enter an opinion you have and we will provide you with papers that matches that opinion" },
        { title: "Save Summaries to Google Drive", description: "Save your generated AI summaries to your Google Drive, for later reference and to keep your research saved for future use" },
    ];

    return (
        <div>
            <div className="background">
            {/* Top Text */}
            <div className="top-left-text">PaperMind</div> 
            {/* Main Home Section */}
            <div className="home-container"
                style={{
                    backgroundImage: `url(${YellowHome})`,  // imported image
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    backgroundRepeat: 'repeat',      
                }}>
                    <Card className="explain-txt-box" sx={{ width:'100%',backgroundColor:'black', color:'white'}}>
                        <CardContent>
                        <Box className="explain-txt" sx={{ textAlign: 'right', padding: '10px 0'}}>
                            <Typography variant="h6" sx={{ fontFamily: 'monospace' }}>
                            Use our AI Assisted Research Assistant to be more productive in your research project. Our assistant is easy to use. Just ask our assistant any topic and it will provide visualisations, summarisations and much more.
                        </Typography>
                        </Box>
                        </CardContent>
                    </Card>
            </div>
        </div> 

        {/* Carousel */}
        <FeatureCarousel featureData={featureData} /> 
        <br></br>
        <div className="search-provider-container">
            <SearchProvider>
                    <div>
                        <SearchComponent />
                    </div>
            </SearchProvider>
        </div>   
        </div>
        
    );
}

export default Home;
