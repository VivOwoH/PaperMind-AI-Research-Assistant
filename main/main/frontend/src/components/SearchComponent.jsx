import React, { useState } from 'react';
import {TextField, Radio, RadioGroup, FormControlLabel, FormControl,FormLabel, Switch, FormGroup, Typography, Button} from '@mui/material';
import '../styles/components/SearchComponent.css'

export default function SearchComponent(){
    // define useState hooks to manage state 
    const [searchQuery, setSearchQuery] = useState('');
    // all / supporting / opposing filters
    const [category, setCategory] = useState('all');

    // print to console for debugging
    const handleSearch = () => {
        console.log('Search Query:', searchQuery);
        console.log('category:', category);
        

        // TODO : Search logic
    };

    

    return (
        <div className="search-container">
        <Typography variant="h6">Search Topic</Typography>
        <TextField
            fullWidth
            variant="outlined"
            placeholder="search prompt goes here"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="search-input"
        />

        <FormControl component="fieldset" className="options-fieldset">
            {/* Radio buttons for filtering by all / supporting / opposing ideas */}
            <FormLabel component="legend">Options</FormLabel>
                {/* set default pick as 'all' */}
                <RadioGroup row value={category} onChange={(e) => setCategory(e.target.value)}>
                    <FormControlLabel value="all" control={<Radio />} label="All" />
                    <FormControlLabel value="supporting" control={<Radio />} label="Supporting" />
                    <FormControlLabel value="opposing" control={<Radio />} label="Opposing" />
                </RadioGroup>
        </FormControl>

        <Button variant="contained" color="primary" onClick={handleSearch}>
            {/* search button */}
            Search
        </Button>
    </div> 
    );

}