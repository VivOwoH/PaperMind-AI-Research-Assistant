import React, { useState } from 'react';
import { Paper, Button } from '@mui/material';
import { BsArrowLeftCircle, BsArrowRightCircle } from 'react-icons/bs'; // Importing arrow icons for navigation

function FeatureCarousel({ featureData }) {
    const [slideIndex, setSlideIndex] = useState(0);

    // Function to go to the next group of 3 cards
    const nextSlide = () => {
        setSlideIndex((prevIndex) => {
            const nextIndex = prevIndex + 3;
            if (nextIndex >= featureData.length) {
                return 0; // Reset to the first slide if we reach the end
            }
            return nextIndex;
        });
    };

    // Function to go to the previous group of 3 cards
    const prevSlide = () => {
        setSlideIndex((prevIndex) => {
            const prevIndexAdjusted = prevIndex - 3;
            if (prevIndexAdjusted < 0) {
                return Math.max(featureData.length - 3, 0); // Go to the last group
            }
            return prevIndexAdjusted;
        });
    };

    // Show 3 items at once in the carousel
    const visibleItems = featureData.slice(slideIndex, slideIndex + 3);

    return (
        <div className="carousel-wrapper" style={{
            display: 'flex', justifyContent: 'center', alignItems: 'center',
            width: '100%', margin: '40px auto', position: 'relative'  // Center horizontally, occupy full width
        }}>
            {/* Left Arrow */}
            <BsArrowLeftCircle
                onClick={prevSlide}
                style={{
                    fontSize: '40px',
                    cursor: 'pointer',
                    position: 'relative',
                    left: '-50px', // Move the arrow further outside the carousel
                    zIndex: 10,
                }}
            />

            {/* Carousel Items */}
            <div className="carousel-container" style={{ display: 'flex', gap: '10px', justifyContent: 'center', width: '80%' }}>
                {visibleItems.map((item, i) => (
                    <Paper key={i} style={{ padding: '20px', margin: '10px', width: '200px', boxShadow: '0px 2px 10px rgba(0,0,0,0.1)'}}>
                        <h2 >{item.title}</h2>
                        <p >{item.description}</p>
                        
                    </Paper>
                ))}
            </div>

            {/* Right Arrow */}
            <BsArrowRightCircle
                onClick={nextSlide}
                style={{
                    fontSize: '40px',
                    cursor: 'pointer',
                    position: 'relative',
                    right: '-50px', // Move the arrow further outside the carousel
                    zIndex: 10,
                }}
            />
        </div>
    );
}

export default FeatureCarousel;
