document.addEventListener('DOMContentLoaded', function () {
    const sliders = [
        { containerId: 'serviziSlider', prevButtonId: 'prevServizi', nextButtonId: 'nextServizi' },
        { containerId: 'fioriSlider', prevButtonId: 'prevFiori', nextButtonId: 'nextFiori' },
        { containerId: 'recensioniSlider', prevButtonId: 'prevRecensioni', nextButtonId: 'nextRecensioni' }
    ];

    sliders.forEach(slider => {
        const sliderWrapper = document.getElementById(slider.containerId);
        const slides = sliderWrapper.querySelectorAll('.slide');
        const prevButton = document.getElementById(slider.prevButtonId);
        const nextButton = document.getElementById(slider.nextButtonId);

        let currentIndex = 0;

        function showSlide(index) {
            const slideWidth = slides[0].clientWidth + parseInt(window.getComputedStyle(slides[0]).marginRight) + parseInt(window.getComputedStyle(slides[0]).marginLeft);
            const maxIndex = slides.length - 3;
            if (index < 0) {
                currentIndex = maxIndex;
            } else if (index > maxIndex) {
                currentIndex = 0;
            } else {
                currentIndex = index;
            }
            const offset = -currentIndex * slideWidth;
            sliderWrapper.style.transform = `translateX(${offset}px)`;
        }

        prevButton.addEventListener('click', () => {
            showSlide(currentIndex - 1);
        });

        nextButton.addEventListener('click', () => {
            showSlide(currentIndex + 1);
        });

        showSlide(currentIndex);
    });
});
