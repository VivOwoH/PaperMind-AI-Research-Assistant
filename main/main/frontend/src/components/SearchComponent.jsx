import React, { useState } from 'react';
import {TextField, Radio, RadioGroup, FormControlLabel, FormControl,InputAdornment,Typography} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import '../styles/components/SearchComponent.css';
import axios from 'axios';
import { useSearch } from '../context/SearchContext';

export default function SearchComponent(){
    // access data and update function from context
    const {searchData,updateSearchData} = useSearch();
    // define useState hooks to manage state 
    const [searchQuery, setSearchQuery] = useState('');
    // all / supporting / opposing filters
    const [category, setCategory] = useState('ALL');
    
    const handleSearch = async () => {
        // update context state with current input values
        updateSearchData('searchPrompt',searchQuery);
        updateSearchData('selectedFilter',category);

        // print to console for debugging
        console.log('search prompt:', searchQuery);
        console.log('selected filter:', category);
        console.log('graph view type:', searchData['graphViewType'])

        // print search object to console
        console.log('search data to send:', searchData);

        // TODO : Search logic
        try{
            // send entire search data object to backend
            const response = await axios.post('http://localhost:8080/api/data', searchData);
            console.log('Server response:', response.data);
        }catch (error){
            console.error('Failed to send data',error);
        }
    };

    return (
        <div>
        {searchData.graphViewType === 'CITATION' && (
            <FormControl component="fieldset" className="options-fieldset">
                {/* Radio buttons for filtering by all / supporting / opposing ideas */}
                <RadioGroup
                    value={category}
                    onChange={(e) => {
                        setCategory(e.target.value);
                        updateSearchData('selectedFilter', e.target.value);
                    }}
                >
                    <FormControlLabel className="radio-label" value="ALL" control={<Radio />} label="All" />
                    <FormControlLabel className="radio-label" value="SUPPORTING" control={<Radio />} label="Supporting" />
                    <FormControlLabel className="radio-label" value="OPPOSING" control={<Radio />} label="Opposing" />
                </RadioGroup>
            </FormControl>
        )}

        <Typography variant="h6">Search Topic</Typography>
        <div className="search-container">
        <TextField
            fullWidth
            variant="outlined"
            placeholder="search prompt goes here"
            value={searchQuery}
            onChange={(e) => {
                setSearchQuery(e.target.value);
                updateSearchData('searchPrompt',e.target.value);
            }}
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