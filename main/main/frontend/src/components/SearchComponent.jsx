import React, { useState } from 'react';
import { TextField, Radio, RadioGroup, Button, FormControlLabel, FormControl, InputAdornment, Typography } from '@mui/material';
import IconButton from '@mui/material/IconButton';
import SearchIcon from '@mui/icons-material/Search';
import '../styles/components/SearchComponent.css';
import CitationOpinionToggle from '../components/CitationOpinionToggle';
import axios from 'axios';
import { useSearch } from '../context/SearchContext';
import { useNavigate } from 'react-router-dom';

export default function SearchComponent() {
    const { searchData, updateSearchData } = useSearch();
    const [searchQuery, setSearchQuery] = useState('');
    const [category, setCategory] = useState('ALL');
    const navigate = useNavigate();
    const [graphPapers, setGraphPapers] = useState([]);
    const [loading, setLoading] = useState(false);

    const handleSearch = async () => {
        updateSearchData('searchPrompt', searchQuery);
        updateSearchData('selectedFilter', category);
        setLoading(true);

        try {
            if (searchData['graphViewType'] === 'CITATION') {
                navigate('papers-graphview', { state: { loading: true } });
            } else if (searchData['graphViewType'] === 'OPINION') {
                navigate('/papers-opinion-graphview', { state: { loading: true } });
            }

            axios.post('http://localhost:8080/api/data', searchData)
                .then(response => {
                    const top20papers = response.data.Papers;
                    const supporting = response.data.supporting;
                    const opposing = response.data.opposing;
                    setGraphPapers(top20papers);

                    if (searchData['graphViewType'] === 'CITATION') {
                        navigate('papers-graphview', { state: { papers: response.data, graphPapers: top20papers, loading: false, graphViewType: searchData['graphViewType'] } });
                    } else if (searchData['graphViewType'] === 'OPINION') {
                        navigate('papers-opinion-graphview', { state: { papers: response.data, graphPapers: top20papers, loading: false, graphViewType: searchData['graphViewType'], supporting, opposing } });
                    }
                })
                .catch(error => {
                    console.error('Failed to fetch data', error);
                    if (searchData['graphViewType'] === 'CITATION') {
                        navigate('papers-graphview', { state: { loading: false, error: true } });
                    } else if (searchData['graphViewType'] === 'OPINION') {
                        navigate('papers-opinion-graphview', { state: { loading: false, error: true } });
                    }
                });
        } catch (error) {
            console.error('Failed to send data', error);
        }
    };

    const handleKeyDown = (e) => {
        if (e.key === 'Enter') {
            handleSearch();
        }
    };

    return (
        <div className="search-container-wrapper">
            <div className="search-content">
                <Typography variant="h4" component="h5" className="search-type" >
                    Hi there, what would you like to know?
                </Typography>
                <br></br>
                <div className="toggle-container">
                    <div className="citation-opinion-toggle">
                        <CitationOpinionToggle />
                    </div>
                    {searchData.graphViewType === 'CITATION' && (
                        <FormControl component="fieldset" className="options-fieldset">
                            <RadioGroup
                                value={category}
                                onChange={(e) => {
                                    setCategory(e.target.value);
                                    updateSearchData('selectedFilter', e.target.value);
                                }}
                            >
                                <FormControlLabel className="radio-label" value="ALL" control={<Radio sx={{ color: 'black', '&.Mui-checked': { color: '#f2bd29' } }} />}  label='All'/>
                                <FormControlLabel className="radio-label" value="SUPPORTING" control={<Radio sx={{ color: 'black', '&.Mui-checked': { color: '#f2bd29' } }} />} label="Supporting" />
                                <FormControlLabel className="radio-label" value="OPPOSING" control={<Radio sx={{ color: 'black', '&.Mui-checked': { color: '#f2bd29' } }} />} label="Opposing" />
                            </RadioGroup>
                        </FormControl>
                    )}
                    
                    <div className="search-container">
                        <TextField
                            fullWidth
                            variant="outlined"
                            placeholder="Ask whatever you want..."
                            value={searchQuery}
                            onChange={(e) => {
                                setSearchQuery(e.target.value);
                                updateSearchData('searchPrompt', e.target.value);
                            }}
                            onKeyDown={handleKeyDown}
                            className="search-input"
                            InputProps={{
                                style: {
                                    width:'100%',
                                    borderRadius: '20px',
                                    color: 'white',
                                    border: '1px solid white',
                                    backgroundColor: 'transparent',
                                },
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton onClick={handleSearch} edge="end" className="search-icon-button">
                                            <SearchIcon />
                                        </IconButton>
                                    </InputAdornment>
                                )
                            }}
                            sx={{
                                
                                '& .MuiOutlinedInput-root.Mui-focused .MuiOutlinedInput-notchedOutline': {
                                    borderColor: 'black',
                                },
                                '& .MuiOutlinedInput-root.Mui-focused': {
                                    boxShadow: 'none',
                                },
                                '& .MuiOutlinedInput-input': {
                                    color: 'black',
                                    backgroundColor: 'transparent',
                                },
                            }}
                        />
                    </div>
                </div>
                
            </div>
            
        </div>
    );
}
