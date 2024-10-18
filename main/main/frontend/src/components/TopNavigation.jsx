// src/components/TopNavigation.jsx
import React from 'react';
import { AppBar, Toolbar, Menu, MenuItem, IconButton, Typography } from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import HomeIcon from '@mui/icons-material/Home';

const TopNavigation = ({ currentView, onViewChange, papers }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const navigate = useNavigate();

    const handleMenuClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = (viewOption) => {
        if (viewOption === 'List View') {
            // Navigate to the List View page when "List View" is selected from the dropdown
            navigate('/list-view', { state: { papers } }); // Redirect to ListViewPage with papers 
        } else {
            onViewChange(viewOption); 
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
                    {/* Dropdown option for List View */}
                    <MenuItem onClick={() => handleMenuClose('List View')}>List View</MenuItem>
                </Menu>
            </Toolbar>
        </AppBar>
    );
};

export default TopNavigation;
