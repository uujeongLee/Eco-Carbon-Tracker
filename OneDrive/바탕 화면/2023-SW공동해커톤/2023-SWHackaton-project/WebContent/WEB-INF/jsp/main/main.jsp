<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>붕어싸만co2</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    
    <meta content="" name="description">
  	<meta content="" name="keywords">
    
      <!-- Favicons -->
  	<link href="assets/img/favicon.png" rel="icon">
  	<link href="assets/img/apple-touch-icon.png" rel="apple-touch-icon">
    
      <!-- Google Fonts -->
	 <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Raleway:300,300i,400,400i,500,500i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">
	
	  <!-- Vendor CSS Files -->
	 <link href="/vendor/aos/aos.css" rel="stylesheet">
	 <link href="/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	 <link href="/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
	 <link href="/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
	 <link href="/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
	 <link href="/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">
	    
      <!-- Template Main CSS File -->
  	<link href="/css/main/style.css" rel="stylesheet">
	
	<!-- gis -->
	<script type="text/javascript" src="/jquery/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="/jquery/jquery-ui.min.js"></script>
	 
    <script type="text/javascript" src="/js/lib/ol.js"></script>
	<link rel="stylesheet" href="/css/lib/ol.css">
 	<link rel="stylesheet" href="/css/main/main.css">
 	
 	
 	<script type="text/javascript" src="/js/main/tool.js"></script>
 	<script type="text/javascript" src="/js/main/main.js"></script>
 	

</head>

<header style="margin-bottom: 0;">
  <div class="inner"></div>
</header>

<body>

  <!-- ======= Mobile nav toggle button ======= -->
  <!-- <button type="button" class="mobile-nav-toggle d-xl-none"><i class="bi bi-list mobile-nav-toggle"></i></button> -->
  <i class="bi bi-list mobile-nav-toggle d-lg-none"></i>
  <!-- ======= Header ======= -->
  <header id="header" class="d-flex flex-column justify-content-center">

    <nav id="navbar" class="navbar nav-menu">
      <ul>
        <li><a href="#hero" class="nav-link scrollto active"><i class="bx bx-home"></i> <span>Home</span></a></li>
        <li><a href="#map" class="nav-link scrollto"><i class="bi bi-map"></i> <span>Map</span></a></li>
        <li><a href="#services" class="nav-link scrollto"><i class="bi bi-search"></i> <span>Search</span></a></li>
      </ul>
    </nav><!-- .nav-menu -->
    
  </header><!-- End Header -->

  <!-- ======= Hero Section ======= -->
  <section id="hero" class="d-flex flex-column justify-content-center">
    <div class="container" data-aos="zoom-in" data-aos-delay="100">
      <h1>Reduce C,</h1>
      <p>Reduce <span class="typed" data-typed-items=" your tax!"></span></p>
    </div>
  </section><!-- End Hero -->




<section id="map" class="d-flex flex-column justify-content-center" style="height:100vh">
  <main id="main">

    <!-- ======= About Section ======= -->
    <div class="container" data-aos="fade-up" style="position: relative;">

      <div class="section-title" style="">
        <h2>지도</h2>
        <!--boot strap 시도중-->
        <select class="getSggListBySd" data-style="btn-primary" name="시/도" style="width:150px; height:50px">
          <option value="선택">시도</option>
          <option value="11">서울특별시</option>
          <option value="26">부산광역시</option>
          <option value="27">대구광역시</option>
          <option value="28">인천광역시</option>
          <option value="29">광주광역시</option>
          <option value="30">대전광역시</option>
          <option value="31">울산광역시</option>
          <option value="36">세종특별자치시</option>
          <option value="41">경기도</option>
          <option value="42">강원도</option>
          <option value="43">충청북도</option>
          <option value="44">충청남도</option>
          <option value="45">전라북도</option>
          <option value="46">전라남도</option>
          <option value="47">경상북도</option>
          <option value="48">경상남도</option>
          <option value="50">제주특별자치도</option>
        </select>


        <select id = "sgg" name="sgg" style="width:150px; height:50px">
          <option value="00">시군구</option>
        </select>&nbsp;&nbsp;
        
        <select id="year" name="year" style="width:100px; height:50px">
          <option value="2013">2013</option>
          <option value="2014">2014</option>
          <option value="2015">2015</option>
          <option value="2016">2016</option>
          <option value="2017">2017</option>
          <option value="2018">2018</option>
          <option value="2019">2019</option>
          <option value="2020">2020</option>
          <option value="2021">2021</option>
          <option value="2022">2022</option>
          <option value="2023">2023</option>
        </select>&nbsp;&nbsp;
        
        <select id="month" name="month" style="width:90px; height:50px">
          <option value="01">1</option>
          <option value="02">2</option>
          <option value="03">3</option>
          <option value="04">4</option>
          <option value="05">5</option>
          <option value="06">6</option>
          <option value="07">7</option>
          <option value="08">8</option>
          <option value="09">9</option>
          <option value="10">10</option>
          <option value="11">11</option>
          <option value="12">12</option>
        </select>&nbsp;&nbsp;
        
        <!--기본 html 확인 버튼 <input type='button' style="width:90px; height:50px; font-weight : bold;" value='확인' />-->
        <!--bootstrap 확인 버튼-->
        <button id="search" type="button" class="btn btn-outline-primary" onclick ="searchBuilding()" style="  width:90px; height:50px; font-weight : bold;">확인</button>
      </div>

      
<!--       <div style="text-align: center;"><img src="assets/img/map.jpeg" width="1000" height="800"></div> -->
      	<div class="side" style="top: 260px; right: -50px;">
	    <ul>
	        <li class="base on"><a href="#void"></a><span>배경지도</span></li>
	
	        <li class="plus"><a href="#void"></a><span>확대</span></li>
	        <li class="minus"><a href="#void"></a><span>축소</span></li>
	        <li class="expand"><a href="#void"></a><span>충남전역</span></li>
	    </ul>
	    <ul>
	        <li class="reduce on"><a href="#void"></a><span>접기</span></li>
	    </ul>
	</div>
	<div id="map" class="map" style="width:1000%; height: 100%;"></div>
	
	<div id="popup" class="ol-popup">
    	<a href="#" id="popup-closer" class="ol-popup-closer"></a>
    	<div id="popup-content"></div>
	</div>
      <div class="row">

      
      </div>&nbsp;

    </div>
  </section><!-- End Facts Section -->


    <section id="services" class="d-flex flex-column justify-content-center">
      <div class="container" data-aos="fade-up">

        <div class="section-title">
          <h2>탄소배출권 가격측정</h2>
          <p>탄소배출권은 기업이나 국가가 일정량의 온실가스를 배출할 수 있는 권한을 나타냅니다. 탄소배출권 가격은 이러한 권한을 시장에서 거래함에 따라 형성되며, 공급과 수요의 상호작용에 따라 변동합니다. 가격은 탄소 배출량에 대한 경제적 가치를 반영하며, 기후변화 문제 해결과 탄소 저감을 장려하기 위한 정책 수단으로 사용됩니다.</p>
        </div>
        <div class="section-title" style="margin-top: 10px;">
          <img src="/img/main/ccrd.jpeg">
        </div>
        
          <script>
            //탄소배출 계산 함수
            function calculate() {
              var x = document.getElementById("input").value; // 사용자가 입력한 변수 x를 가져옴
              var result = x * 16230; // 계산 결과를 저장
              
              // 결과를 HTML 요소에 출력
              document.getElementById("output").textContent = "탄소배출권 가격: " + result + "원";
            }
          </script>
          <div class="section-title" style="margin-top: 0px;">
            
          <input type="number" id="input" style="height: 40px; margin-right: 40px;" 
          placeholder="탄소사용량(단위:ton)  "
          onfocus="placeholder=''" 
          onblur="placeholder='탄소사용량(단위:ton)'"
          &nbsp;/>
          
          <button onclick="calculate()" type="button" class="btn btn-outline-primary" style="  width:90px; height:45px; font-weight : bold;">계산</button>
          <br>
          <br>
          <br>
          <p id="output" style = "font-weight : bold; font-size: 30px;"></p>
          </div>
          
        
      </div>
    </section><!— End Facts Section —>

    
          </div>
          <div class="swiper-pagination"></div>
        </div>

      </div>
    </section><!-- End Testimonials Section -->

    <!-- ======= Contact Section ======= -->


  </main><!-- End #main -->








  
  <!-- ======= Footer ======= -->
  <footer id="footer">
    <div class="container">
      
      <div class="copyright">
        &copy; Copyright <strong><span>bungeossamanco2</span></strong>. All Rights Reserved
      </div>
      <div class="credits">
        Designed by bungeossamanco2
      </div>
    </div>
  </footer><!-- End Footer -->

  <div id="preloader"></div>
  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"><i class="bi bi-arrow-up-short"></i></a>

  <!-- Vendor JS Files -->
  <script src="/vendor/purecounter/purecounter_vanilla.js"></script>
  <script src="/vendor/aos/aos.js"></script>
  <script src="/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
  <script src="/vendor/glightbox/js/glightbox.min.js"></script>
  <script src="/vendor/isotope-layout/isotope.pkgd.min.js"></script>
  <script src="/vendor/swiper/swiper-bundle.min.js"></script>
  <script src="/vendor/typed.js/typed.umd.js"></script>
  <script src="/vendor/waypoints/noframework.waypoints.js"></script>
  <script src="/vendor/php-email-form/validate.js"></script>
  <!-- Template Main JS File -->
  <script type="text/javascript" src="/js/main/main2.js"></script>

</body>

</html>