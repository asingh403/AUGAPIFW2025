allure.api.addTab('overview', {
    title: 'Custom Overview', 
    icon: 'fa fa-home',
    route: 'custom-overview',
    onEnter: () => {
        // Inject custom CSS and JS
        const customCSS = document.createElement('link');
        customCSS.rel = 'stylesheet';
        customCSS.href = 'plugins/custom-logo-plugin/static/custom.css';
        document.head.appendChild(customCSS);

        const customJS = document.createElement('script');
        customJS.src = 'plugins/custom-logo-plugin/static/custom.js';
        document.head.appendChild(customJS);
    }
});