#!/bin/bash

# Allure Report Customizer Script
# This script generates fresh Allure reports with custom logo, confetti, and styling

set -e  # Exit on any error

# Configuration
ALLURE_RESULTS_DIR="allure-results"
ALLURE_REPORT_DIR="allure-report"
LOGO_SOURCE="src/test/resources/src/test/resources/logo.png"
PLUGIN_DIR="$ALLURE_REPORT_DIR/plugins/custom-logo-plugin/static"
CUSTOM_HTML_TEMPLATE="custom-allure-template.html"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if required files exist
check_prerequisites() {
    print_status "Checking prerequisites..."
	    
    if [ ! -d "$ALLURE_RESULTS_DIR" ]; then
        print_error "Directory $ALLURE_RESULTS_DIR not found!"
        exit 1
    fi
    
    if [ ! -f "$LOGO_SOURCE" ]; then
        print_error "Logo file not found at $LOGO_SOURCE"
        exit 1
    fi
    
    if ! command -v allure &> /dev/null; then
        print_error "Allure command not found. Please install Allure."
        exit 1
    fi
    
    print_success "All prerequisites met"
}

# Function to create custom HTML template if it doesn't exist
create_custom_template() {
    if [ ! -f "$CUSTOM_HTML_TEMPLATE" ]; then
        print_status "Creating custom HTML template..."
        cat > "$CUSTOM_HTML_TEMPLATE" << 'EOF'
<!DOCTYPE html>
<html dir="ltr" lang="en">
<head>
    <meta charset="utf-8">
    <title>AS-Tech-Allure Report</title>
    <link rel="icon" href="favicon.ico">
    <link rel="stylesheet" type="text/css" href="styles.css">
    <link rel="stylesheet" type="text/css" href="plugin/screen-diff/styles.css">
    
    <style>
    .confetti { pointer-events: none; }
    .firework { pointer-events: none; }
    .celebration-message {
        position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%);
        background: linear-gradient(45deg, #ff6b6b, #4ecdc4, #45b7d1);
        color: white; padding: 20px 40px; font-size: 24px; font-weight: bold;
        border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.3);
        z-index: 10001; animation: celebration-bounce 2s ease-in-out;
    }
    @keyframes celebration-bounce {
        0%, 20%, 60%, 100% { transform: translate(-50%, -50%) translateY(0); }
        40% { transform: translate(-50%, -50%) translateY(-20px); }
        80% { transform: translate(-50%, -50%) translateY(-10px); }
    }
    </style>
</head>
<body>
    <img src="plugins/custom-logo-plugin/static/logo.png" alt="CAPCO Logo" 
         style="position: fixed; top: 0px; left: 0px; width: 178px; height: 76px; z-index: 9999;">
    <div id="alert"></div>
    <div id="content"><span class="spinner"><span class="spinner__circle"></span></span></div>
    <div id="popup"></div>
    <script src="app.js"></script>
    <script src="plugin/behaviors/index.js"></script>
    <script src="plugin/packages/index.js"></script>
    <script src="plugin/screen-diff/index.js"></script>
    <script async src="https://www.googletagmanager.com/gtag/js?id=G-FVWC4GKEYS"></script>
    <script>
        window.dataLayer = window.dataLayer || [];
        function gtag(){dataLayer.push(arguments);}
        gtag('js', new Date());
        gtag('config', 'G-FVWC4GKEYS', {
          'allureVersion': '2.34.1', 'reportUuid': 'PLACEHOLDER', 'single_file': false
        });
    </script>
    <script>
        function changeReportTitle() {
            const currentDate = new Date().toLocaleDateString('en-GB', {
                day: '2-digit', month: 'short', year: 'numeric'
            }).toUpperCase().replace(/,/g, '');
            const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);
            let node;
            while (node = walker.nextNode()) {
                if (node.textContent.includes('ALLURE REPORT') || node.textContent.includes('Allure Report')) {
                    node.textContent = `Execution-Report | ${currentDate}`;
                }
            }
        }
        function checkForCelebration() {
            setTimeout(function() {
                let passRate = 0;
                document.querySelectorAll('*').forEach(element => {
                    const text = element.textContent;
                    if (text.includes('%') && !element.children.length) {
                        const percentage = text.match(/(\d+\.?\d*)%/);
                        if (percentage) passRate = Math.round(parseFloat(percentage[1]));
                    }
                });
                if (passRate >= 60) startCelebration(passRate);
            }, 3000);
        }
        function startCelebration(passRate) {
            const message = document.createElement('div');
            message.className = 'celebration-message';
            if (passRate === 100) message.innerHTML = '🎉 PERFECT! 100% TESTS PASSED! 🎉';
            else if (passRate >= 90) message.innerHTML = '🌟 EXCELLENT! ' + passRate + '% TESTS PASSED! 🌟';
            else if (passRate >= 80) message.innerHTML = '👏 GREAT JOB! ' + passRate + '% TESTS PASSED! 👏';
            else message.innerHTML = '✨ GOOD WORK! ' + passRate + '% TESTS PASSED! ✨';
            document.body.appendChild(message);
            for (let i = 0; i < 500; i++) setTimeout(() => createConfetti(), i * 15);
            setTimeout(() => createFirework(200, 200), 500);
            setTimeout(() => createFirework(window.innerWidth - 200, 300), 1000);
            setTimeout(() => createFirework(window.innerWidth / 2, 250), 1500);
            setTimeout(() => {
                message.remove();
                document.querySelectorAll('.confetti, .firework').forEach(el => el.remove());
            }, 8000);
        }
        function createConfetti() {
            const confetti = document.createElement('div');
            confetti.className = 'confetti';
            const startX = Math.random() * window.innerWidth;
            const velocity = 25 + Math.random() * 25;
            const horizontalSpeed = (Math.random() - 0.5) * 60;
            confetti.style.cssText = `position: fixed; left: ${startX}px; top: -20px; 
                width: ${Math.random() * 10 + 8}px; height: ${Math.random() * 10 + 8}px; z-index: 10000;
                border-radius: ${Math.random() > 0.5 ? '50%' : '0'};`;
            const colors = ['#ff6b6b', '#4ecdc4', '#45b7d1', '#96ceb4', '#ffeaa7', '#dda0dd'];
            confetti.style.background = colors[Math.floor(Math.random() * colors.length)];
            document.body.appendChild(confetti);
            let currentY = -20, currentX = startX, currentVelocity = velocity, opacity = 1;
            const animate = () => {
                currentY += currentVelocity; currentX += horizontalSpeed * 0.1;
                currentVelocity += 0.8; opacity -= 0.004;
                confetti.style.top = currentY + 'px'; confetti.style.left = currentX + 'px';
                confetti.style.opacity = opacity;
                if (currentY < window.innerHeight + 100 && opacity > 0) requestAnimationFrame(animate);
                else confetti.remove();
            };
            requestAnimationFrame(animate);
        }
        function createFirework(x, y) {
            for (let i = 0; i < 25; i++) {
                const particle = document.createElement('div');
                particle.className = 'firework';
                const angle = (i / 25) * Math.PI * 2;
                const velocity = Math.random() * 6 + 4;
                const vx = Math.cos(angle) * velocity, vy = Math.sin(angle) * velocity;
                particle.style.cssText = `position: fixed; left: ${x}px; top: ${y}px; 
                    width: 6px; height: 6px; border-radius: 50%; z-index: 10002;`;
                const colors = ['#fff', '#ffff00', '#ff6b6b', '#4ecdc4', '#45b7d1'];
                const color = colors[Math.floor(Math.random() * colors.length)];
                particle.style.background = color; particle.style.boxShadow = `0 0 15px ${color}`;
                document.body.appendChild(particle);
                let currentX = x, currentY = y, currentVx = vx, currentVy = vy, opacity = 1;
                const animateFirework = () => {
                    currentX += currentVx; currentY += currentVy; currentVy += 0.15; 
                    currentVx *= 0.98; opacity -= 0.015;
                    particle.style.left = currentX + 'px'; particle.style.top = currentY + 'px';
                    particle.style.opacity = opacity;
                    if (opacity > 0 && currentY < window.innerHeight + 50) requestAnimationFrame(animateFirework);
                    else particle.remove();
                };
                setTimeout(() => requestAnimationFrame(animateFirework), i * 30);
            }
        }
        document.addEventListener('DOMContentLoaded', function() {
            setTimeout(function() { changeReportTitle(); checkForCelebration(); }, 2000);
        });
    </script>
</body>
</html>
EOF
        print_success "Custom HTML template created"
    fi
}

# Function to update reportUuid in template
update_template() {
    if [ -f "$ALLURE_REPORT_DIR/index.html" ]; then
        local uuid=$(grep -o '"reportUuid": "[^"]*"' "$ALLURE_REPORT_DIR/index.html" | cut -d'"' -f4)
        if [ ! -z "$uuid" ]; then
            sed -i.bak "s/PLACEHOLDER/$uuid/g" "$CUSTOM_HTML_TEMPLATE"
            rm -f "$CUSTOM_HTML_TEMPLATE.bak"
        fi
    fi
}

# Main execution function
main() {
    print_status "Starting Allure Report Customization..."
    echo "================================================"
    
    # Step 1: Check prerequisites
    check_prerequisites
    
    # Step 2: Generate fresh Allure report
    print_status "Generating fresh Allure report..."
    allure generate "$ALLURE_RESULTS_DIR/" --output-dir "$ALLURE_REPORT_DIR" --clean
    print_success "Allure report generated"
    
    # Step 3: Create plugin directory structure
    print_status "Setting up plugin directory structure..."
    mkdir -p "$PLUGIN_DIR"
    print_success "Plugin directory created"
    
    # Step 4: Copy logo
    print_status "Copying company logo..."
    cp "$LOGO_SOURCE" "$PLUGIN_DIR/logo.png"
    print_success "Logo copied"
    
    # Step 5: Create custom template
    create_custom_template
    
    # Step 6: Update template with current UUID
    update_template
    
    # Step 7: Apply customizations
    print_status "Applying customizations..."
    cp "$CUSTOM_HTML_TEMPLATE" "$ALLURE_REPORT_DIR/index.html"
    print_success "Customizations applied"
    
    # Step 8: Open report
    print_status "Opening customized Allure report..."
    allure open "$ALLURE_REPORT_DIR/"
    
    echo "================================================"
    print_success "Customized Allure report is ready!"
    print_status "Features included:"
    echo "  - CAPCO company logo"
    echo "  - Dynamic title with current date"
    echo "  - Confetti celebration for ≥60% pass rate"
    echo "  - Firework animations for 100% pass rate"
}

# Run main function
main "$@"
