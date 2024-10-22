// SearchContext.jsx
import React, {createContext, useContext, useState} from 'react';

// create a context for storing all required data from homepage 
const SearchContext = createContext();
export const useSearch = () => useContext(SearchContext);

export const SearchProvider = ({children}) => {
    const [searchData, setSearchData] = useState({
        // default data
        searchPrompt: '',
        selectedFilter:'ALL',
        viewPreference: 'GRAPH',
        graphViewType:'CITATION' 
    });

    //update the combined data
    const updateSearchData = (key,value) => {
        setSearchData(prevData => ({...prevData, [key]:value}));
    };

    return(
        <SearchContext.Provider value={{searchData, updateSearchData}}>
            {children}
        </SearchContext.Provider>
    );
}

