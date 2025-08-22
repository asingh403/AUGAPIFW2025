// Custom JavaScript for Allure Reports

// Wait for page to load
document.addEventListener('DOMContentLoaded', function() {
    addCustomHeader();
});

// Also trigger when Allure navigation changes
window.addEventListener('hashchange', function() {
    setTimeout(addCustomHeader, 500);
});

function addCustomHeader() {
    // Check if custom header already exists
    if (document.querySelector('.custom-header')) {
        return;
    }
    
    // Create custom header with logo
    const customHeader = document.createElement('div');
    customHeader.className = 'custom-header';
    customHeader.innerHTML = `
        <img src="plugins/custom-logo-plugin/static/logo.png" alt="MasterCard Logo" />
        <h1>API Test Automation Framework [TAF] - Ashutosh</h1>
    `;
    
    // Find the main content area and prepend header
    const mainContent = document.querySelector('.app__content') || 
                       document.querySelector('.main-content') ||
                       document.querySelector('body');
    
    if (mainContent) {
        mainContent.insertBefore(customHeader, mainContent.firstChild);
    }
}

// Initialize when script loads
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', addCustomHeader);
} else {
    addCustomHeader();
}