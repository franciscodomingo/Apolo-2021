(function ($) {
    "use strict";
    
    
    
    
    // Sticky Navbar
    $(window).scroll(function () {
        if ($(this).scrollTop() > 90) {
            $('.nav-bar').addClass('nav-sticky');
            $('.carousel, .page-header').css("margin-top", "73px");
        } else {
            $('.nav-bar').removeClass('nav-sticky');
            $('.carousel, .page-header').css("margin-top", "0");
        }
    });
    
    
    // Dropdown on mouse hover
    $(document).ready(function () {
        function toggleNavbarMethod() {
            if ($(window).width() > 992) {
                $('.navbar .dropdown').on('mouseover', function () {
                    $('.dropdown-toggle', this).trigger('click');
                }).on('mouseout', function () {
                    $('.dropdown-toggle', this).trigger('click').blur();
                });
            } else {
                $('.navbar .dropdown').off('mouseover').off('mouseout');
            }
        }
        toggleNavbarMethod();
        $(window).resize(toggleNavbarMethod);
    });

      // Swiper Portada
    var swiper = new Swiper(".mySwiper", {
        autoplay: {
            delay: 7000,
          },
        allowTouchMove: false,
        speed: 1000,
        autoHeight: false,
        loop: true
    });
    
      // Swiper Votados
      var swiper = new Swiper(".mySwiper2", {
        slidesPerView: 6,
        spaceBetween: 50,
        loop: true,
        autoplay: true,
        grabCursor: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
      });
      // Swiper Categorias 1
      var swiper = new Swiper(".mySwiper3", {
        effect: 'coverflow',
        coverflowEffect: {
          rotate: 30,
          slideShadows: false,
        },
        slidesPerView: 6,
        spaceBetween: 10,
        loop: true,
        grabCursor: true,
        pagination: {
          el: ".swiper-pagination",
          clickable: true,
        },
      });
    
    
})(jQuery);

$('.form_datetime').datetimepicker({
    language:  'es',
    weekStart: 1,
    todayBtn:  1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1
});

$(function() {
	var $dropDownMenu = $(".avatar-dropdown-menu");

	$dropDownMenu.click(function(e) {		
		e.stopPropagation();

		$(document).on("click", menuCloseListener);

		toggleMenu();
	});

	var toggleMenu = function() {
		$dropDownMenu.toggleClass("open");
	}

	var menuCloseListener = function() {
		toggleMenu();

		$(document).off("click", menuCloseListener);
	}
});

$(function() {
	var $dropDownMenu = $(".noti-dropdown-menu");

	$dropDownMenu.click(function(e) {		
		e.stopPropagation();

		$(document).on("click", menuCloseListener);

		toggleMenu();
	});

	var toggleMenu = function() {
		$dropDownMenu.toggleClass("open");
	}

	var menuCloseListener = function() {
		toggleMenu();

		$(document).off("click", menuCloseListener);
	}
});
$(function() {
	var $dropDownMenu = $(".noti-dropdown-menu2");

	$dropDownMenu.click(function(e) {		
		e.stopPropagation();

		$(document).on("click", menuCloseListener);

		toggleMenu();
	});

	var toggleMenu = function() {
		$dropDownMenu.toggleClass("open");
	}

	var menuCloseListener = function() {
		toggleMenu();

		$(document).off("click", menuCloseListener);
	}
});
