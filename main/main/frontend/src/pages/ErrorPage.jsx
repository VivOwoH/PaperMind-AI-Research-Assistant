import React from 'react';
import { Container, Typography, Button, Box } from '@mui/material';
import ErrorOutlineIcon from '@mui/icons-material/ErrorOutline';

const ErrorPage = ({ onRetry }) => {
    return (
        <Container maxWidth="sm" style={{ textAlign: 'center', marginTop: '20vh' }}>
            <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <ErrorOutlineIcon sx={{ fontSize: 60, color: 'darkred', marginBottom: '20px' }} /> {/* Error icon */}
                <Typography 
                    variant="h4" 
                    style={{ 
                        fontWeight: 'bold', 
                        color: 'darkred', 
                        marginBottom: '20px', 
                        letterSpacing: '2px' // Add spacing for the title text
                    }} 
                    gutterBottom
                >
                    Something Went Wrong
                </Typography>
                <Typography 
                    variant="h6" 
                    style={{ 
                        color: 'black', 
                        marginBottom: '15px',
                        fontWeight: '300' 
                    }} 
                    gutterBottom
                >
                    We encountered an error while processing your request.
                </Typography>
                <Typography 
                    variant="body1" 
                    style={{ 
                        color: '#a4afbf', 
                        letterSpacing: '1px', 
                        marginBottom: '30px' 
                    }} 
                    gutterBottom
                >
                    Please try again or contact support if the issue persists.
                </Typography>
    
                <Button 
                    variant="outlined" 
                    sx={{ color: 'darkred', borderColor: 'darkred' }}
                    href="/" // Redirect to homepage or a safe page
                >
                    Go Back
                </Button>
            </Box>
        </Container>
    );
};

export default ErrorPage;
