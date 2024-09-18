import React from 'react';
import '../../styles/pages/Home/Home.css'
import '../../components/SearchComponent'
import SearchComponent from '../../components/SearchComponent';
import CitationOpinionToggle from '../../components/CitationOpinionToggle';
import {SearchProvider} from '../../context/SearchContext';

function Home() {

    return (
            <SearchProvider>
                <div className="home">
                    <CitationOpinionToggle />
                    <SearchComponent /> 
                </div>
            </SearchProvider>  
    );
}

export default Home;
