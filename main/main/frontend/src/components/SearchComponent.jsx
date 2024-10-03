import React, { useState } from 'react';
import {TextField, Radio, RadioGroup, FormControlLabel, FormControl,FormLabel,InputAdornment,Typography,CircularProgress} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import '../styles/components/SearchComponent.css';
import axios from 'axios';
import { useSearch } from '../context/SearchContext';
import { useNavigate } from 'react-router-dom'; // Import useNavigate

export default function SearchComponent(){
    // access data and update function from context
    const {searchData,updateSearchData} = useSearch();
    // define useState hooks to manage state 
    const [searchQuery, setSearchQuery] = useState('');
    // all / supporting / opposing filters
    const [category, setCategory] = useState('ALL');
    // for routes for naviagation
    const navigate = useNavigate();
    // for graph papers
    const [graphPapers,setGraphPapers] = useState([]);
    const [loading, setLoading] = useState(false); 
    
    
    const handleSearch = async () => {
        // update context state with current input values
        updateSearchData('searchPrompt',searchQuery);
        updateSearchData('selectedFilter',category);

        // print to console for debugging
        console.log('search prompt:', searchQuery);
        console.log('selected filter:', category);
        console.log('graph view type:', searchData['graphViewType'])
        
        setLoading(true); // set loading to true before the request

        // print search object to console
        console.log('search data to send:', searchData);

        // TODO : Search logic
        try{
            // navigate to target page immediately, passing empty state initially
            navigate('papers-graphview', { state: { loading: true } } );

            // perform the search request after navigation
            axios.post('http://localhost:8080/api/data', searchData)
                .then(response => {
                
                // obtain the top 20 papers to display in frontend
                const top20papers = response.data.slice(0,20);
                setGraphPapers(top20papers);

                console.log('Server response:', response.data);

                // Update with the actual results once the API call completes
                navigate('papers-graphview', { state: { papers: response.data, graphPapers:top20papers, loading: false } });
            })
            .catch(error => {
                console.error('Failed to fetch data', error);
                navigate('papers-graphview', { state: { loading: false, error: true } });
            });

            // // send entire search data object to backend
            // const response = await axios.post('http://localhost:8080/api/data', searchData);
            
            // // obtain the top 20 papers to display in frontend
            // const top20papers = response.data.slice(0,20);
            // setGraphPapers(top20papers);

            // // navigate to /papers-graphview page and pass API response as state
            // navigate('papers-graphview', {state: {papers:response.data, graphPapers:top20papers}});

        }catch (error){
            console.error('Failed to send data',error);
        }
    };

    return (
        <div>
        <div>
        <FormControl component="fieldset" className="options-fieldset">
            {/* Radio buttons for filtering by all / supporting / opposing ideas */}
            <FormLabel component="legend"></FormLabel>
                {/* set default pick as 'all' */}
                <RadioGroup value={category} onChange={(e) =>
                     {setCategory(e.target.value);
                        updateSearchData('selectedFilter',e.target.value);
                     }}>
                    <FormControlLabel className="radio-label" value="ALL" control={<Radio />} label="All" style={{ paddingBottom: '2px' }}/>
                    <FormControlLabel className="radio-label" value="SUPPORTING" control={<Radio />} label="Supporting" style={{ paddingBottom: '2px' }} />
                    <FormControlLabel className="radio-label"  value="OPPOSING" control={<Radio />} label="Opposing" style={{ paddingBottom: '2px' }} />
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