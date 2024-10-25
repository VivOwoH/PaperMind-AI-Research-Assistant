// src/components/TopNavigation.jsx
import React from 'react';
import { AppBar, Toolbar, Menu, MenuItem, IconButton, Typography } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import HomeIcon from '@mui/icons-material/Home';

const TopNavigation = ({ currentView, onViewChange, papers, viewtype, supporting, opposing, prompt, citations }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const navigate = useNavigate();

    const handleMenuClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = (viewOption) => {
        if (viewOption === 'List View') {
            onViewChange('List View');
            // Navigate to the List View page when "List View" is selected from the dropdown
            if (viewtype === 'CITATION'){
                console.log(prompt);
                navigate('/list-view', { state: { papers, prompt, currentView:'List View', supporting, opposing, citations} }); // Redirect to ListViewPage with papers
            }else if (viewtype === 'OPINION'){ 
                navigate('/opinion-list-view', { state: { papers, supporting, opposing, prompt, currentView:'List View'} }); 
            }
        } else if (viewOption === 'Graph View') {
            onViewChange('Graph View');
            if (viewtype === 'CITATION'){
                navigate('/papers-graphview', { state: { papers, prompt, currentView:'Graph View', supporting, opposing, citations} }); // Redirect to ListViewPage with papers
            }else if (viewtype === 'OPINION'){ 
                navigate('/papers-opinion-graphview', { state: { papers, supporting, opposing, prompt, currentView:'Graph View'} }); 
            }
        }
        setAnchorEl(null);
    };

    return (
        <AppBar position="static" style={{ backgroundColor: '#302C29' }}>
            <Toolbar>
                {/* Home icon linking to the homepage */}
                <IconButton component={Link} to="/" color="inherit">
                    <HomeIcon />
                </IconButton>

                <div style={{ flexGrow: 1 }} />

                {/* Display current view */}
                <Typography
                    variant="h6"
                    style={{ marginRight: '10px', color: '#fff' }} 
                >
                    {currentView} {/* This will display the current view */}
                </Typography>

                {/* Dropdown menu for toggling view */}
                <IconButton
                    aria-controls="simple-menu"
                    aria-haspopup="true"
                    onClick={handleMenuClick}
                    color="inherit"
                >
                    <ArrowDropDownIcon />
                </IconButton>
                <Menu
                    id="simple-menu"
                    anchorEl={anchorEl}
                    keepMounted
                    open={Boolean(anchorEl)}
                    onClose={() => setAnchorEl(null)} // close without action
                    PaperProps={{
                        style: {
                            backgroundColor: '#333', 
                            color: '#fff',
                        },
                    }}
                >
                    {/* Dropdown option for current View */}
                    {currentView === 'List View'?(
                            <MenuItem onClick={() => handleMenuClose('Graph View')}>Graph View</MenuItem>
                    ) : (
                            <MenuItem onClick={() => handleMenuClose('List View')}>List View</MenuItem>
                    )}
                </Menu>
            </Toolbar>
        </AppBar>
    );
};

export default TopNavigation;
