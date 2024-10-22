import React from 'react';
import { Container, Typography, CircularProgress, Box } from '@mui/material';

const LoadingPage = () => {
    return (
        <Container maxWidth="sm" style={{ textAlign: 'center', marginTop: '20vh' }}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
            <Typography 
                    variant="h4" 
                    style={{ 
                        fontWeight: 'bold', 
                        color: 'darkblue', 
                        marginBottom: '20px',
                        letterSpacing: '2px' // Add spacing for the title text
                    }} 
                    gutterBottom
                >
                    PaperMind
                </Typography>
                <br></br>
                <CircularProgress size={60} sx={{ color: 'darkblue' }}/> {/* Spinner */}
                <br></br>
                <br></br>
                <Typography 
                    variant="h5" 
                    style={{ 
                        color: 'black',
                        marginBottom: '10px',
                        fontWeight: '300' 
                    }} 
                    gutterBottom
                >
                    Fetching Results...
                </Typography>
                <Typography 
                    variant="body1" 
                    style={{ 
                        color: '#a4afbf', 
                        letterSpacing: '1px' 
                    }} 
                    gutterBottom
                >
                    Please wait a moment while we retrieve your results.
                </Typography>
                
            </Box>
        </Container>
    );
};

export default LoadingPage;