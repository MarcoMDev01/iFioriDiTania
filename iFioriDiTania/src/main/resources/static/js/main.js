let autoSlideInterval;

function showSlide(wrapperId, index) {
    const carouselWrapper = document.getElementById(wrapperId);
    const totalSlides = carouselWrapper.children.length;

    carouselWrapper.style.transition = 'none';
    carouselWrapper.appendChild(carouselWrapper.children[0]);
    carouselWrapper.style.transform = 'translateX(0)';
    currentSlide = totalSlides - 1;
}

function nextSlide(wrapperId) {
    const carouselWrapper = document.getElementById(wrapperId);
    const totalSlides = carouselWrapper.children.length;

    carouselWrapper.style.transition = 'none';
    carouselWrapper.appendChild(carouselWrapper.children[0]);
    carouselWrapper.style.transform = 'translateX(0)';
    currentSlide = totalSlides - 1;
}

function prevSlide(wrapperId) {
    const carouselWrapper = document.getElementById(wrapperId);
    const totalSlides = carouselWrapper.children.length;

    carouselWrapper.insertBefore(carouselWrapper.children[totalSlides - 1], carouselWrapper.children[0]);

    currentSlide = (currentSlide - 1 + totalSlides) % totalSlides;

    carouselWrapper.style.transition = 'none';
    carouselWrapper.style.transform = `translateX(0)`;
}

function startAutoSlide(wrapperId) {
    autoSlideInterval = setInterval(() => nextSlide(wrapperId), 2000);
}

function stopAutoSlide() {
    clearInterval(autoSlideInterval);
}

document.addEventListener('DOMContentLoaded', () => {
    showSlide('carouselWrapper1', 0);
    showSlide('carouselWrapper2', 0);
    startAutoSlide('carouselWrapper1');
    startAutoSlide('carouselWrapper2');

    document.querySelector('#carouselContainer1').addEventListener('mouseover', stopAutoSlide);
    document.querySelector('#carouselContainer1').addEventListener('mouseout', () => startAutoSlide('carouselWrapper1'));

    document.querySelector('#carouselContainer2').addEventListener('mouseover', stopAutoSlide);
    document.querySelector('#carouselContainer2').addEventListener('mouseout', () => startAutoSlide('carouselWrapper2'));
});
