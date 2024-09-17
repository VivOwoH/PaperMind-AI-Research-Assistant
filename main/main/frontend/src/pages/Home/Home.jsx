import React from 'react';
import '../../styles/pages/Home/Home.css'
import '../../components/SearchComponent'
import SearchComponent from '../../components/SearchComponent';
import CitationOpinionToggle from '../../components/CitationOpinionToggle';

function Home() {
    return (
        <div className="home">
            {/*add the search component*/}
            <SearchComponent />
            <CitationOpinionToggle />
            
        </div>
    );
}

export default Home;
