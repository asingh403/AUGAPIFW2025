// Simple Custom JS for ChainTest

// Add logo when page loads
document.addEventListener('DOMContentLoaded', function() {
    addLogo();
});

function addLogo() {
    // Create logo image
    var logo = document.createElement('img');
    logo.src = 'file:///Users/ashutoshsingh/eclipse-workspace/AUGAPIFW2025/src/test/resources/MasterCard.png';
    logo.style.width = '120px';
    logo.style.height = 'auto';
    logo.style.maxHeight = '50px';
    logo.style.marginRight = '15px';
    logo.style.verticalAlign = 'middle';
    logo.alt = 'MasterCard Logo';
    
    // Find header and add logo
    var header = document.querySelector('h1') || 
                 document.querySelector('.navbar-brand') || 
                 document.querySelector('.container');
    
    if (header) {
        header.insertBefore(logo, header.firstChild);
    }
}