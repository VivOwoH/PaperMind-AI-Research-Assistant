import React, { useState } from 'react';
import {TextField, Radio, RadioGroup, FormControlLabel, FormControl,FormLabel,InputAdornment,Typography} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
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
        <div>
        <div>
        <FormControl component="fieldset" className="options-fieldset">
            {/* Radio buttons for filtering by all / supporting / opposing ideas */}
            <FormLabel component="legend"></FormLabel>
                {/* set default pick as 'all' */}
                <RadioGroup value={category} onChange={(e) => setCategory(e.target.value)}>
                    <FormControlLabel className="radio-label" value="all" control={<Radio />} label="All" style={{ paddingBottom: '2px' }}/>
                    <FormControlLabel className="radio-label" value="supporting" control={<Radio />} label="Supporting" style={{ paddingBottom: '2px' }} />
                    <FormControlLabel className="radio-label"  value="opposing" control={<Radio />} label="Opposing" style={{ paddingBottom: '2px' }} />
                </RadioGroup>
        </FormControl>
        </div>

        <Typography variant="h6">Search Topic</Typography>
        <div className="search-container">
        <TextField
            fullWidth
            variant="outlined"
            placeholder="search prompt goes here"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="search-input"
            InputProps={{
            endAdornment: (
            <InputAdornment position="end">
            <IconButton
                onClick={handleSearch}
                edge="end"
            >
            <SearchIcon />
            </IconButton>
            </InputAdornment>
            )
            }}
            />
        </div>

    </div>
    );

}