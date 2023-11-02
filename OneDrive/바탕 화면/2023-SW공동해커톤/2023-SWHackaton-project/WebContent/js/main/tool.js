function sideChangeDimension() {
    //경기
    $(".side .expand").on("click", function(){
        map.getView().setCenter(new ol.geom.Point([126.988220, 37.550565]).transform('EPSG:4326', 'EPSG:3857').getCoordinates());
        map.getView().setZoom(9);
    });

    //지도 변경
    $(".side .base").on("click", function() {

        if($(".side .base").hasClass("on")) {
        	baseMap.setSource(satelliteMap);
            $(".side .base").removeClass("on");
        } else {
        	baseMap.setSource(colorMap);
            $(".side .base").addClass("on");
        }

    });

    $(".side .view").on("click", function() { 
        if(!map3D) return false;

        resetSettings();
        $(this).toggleClass("on");
        if($(this).hasClass("on")) {
            currentDimension = "3D";
            bldgFilterApply();

            if($(".side .base").hasClass("on")) {
                /*$(".side .base").toggleClass("on"); 배경지도 상태 변화*/
                $(".side .base").removeClass("on");
            }

            changeDimension(currentDimension);
            Boundary3Dchecked();
            boundaryChangeEvent();
            if($(".side .base").hasClass("on")) {
                map3D.imageryLayers.removeAll();
                map3D.imageryLayers.addImageryProvider(new Cesium.ArcGisMapServerImageryProvider({url: 'https://services.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer'}));
                map3D.imageryLayers.get(0).alpha = 0.8;
                //layers.addImageryProvider(gSggProvider);
            } else {
                map3D.imageryLayers.removeAll();
                map3D.imageryLayers.addImageryProvider(new Cesium.ArcGisMapServerImageryProvider({url: 'https://services.arcgisonline.com/arcgis/rest/services/Canvas/World_Dark_Gray_Base/MapServer'}));
                map3D.imageryLayers.get(0).alpha = 0.8;
                //layers.addImageryProvider(gSggProvider);
            }
            document.querySelector("div.no-use").style.display = 'none';

            if(document.getElementById("windType2").checked) {
                document.querySelector(".weather-menu-item.alt").classList.add('on');
                document.querySelector(".weather-menu-item.panel").classList.add('on');
                $(".weather-menu-item.grid").css('display', 'none');
            }
        } else {
            currentDimension = "2D";
            changeDimension(currentDimension);
            document.querySelector("div.no-use").style.display = '';

            //3D 날씨 정보에서 2D로 전환 되었을 때 연직고도 표출 방지
            document.querySelector(".weather-menu-item.alt").classList.remove('on');
            document.querySelector(".weather-menu-item.panel").classList.remove('on');
            $(".weather-menu-item.grid").css('display', 'flex');
        }
    });
}

function mapZoomControl() {
    document.querySelector(".side .plus").addEventListener('click', function() {
        map.getView().setZoom(map.getView().getZoom()+1);
    });
    document.querySelector(".side .minus").addEventListener('click', function() {
        map.getView().setZoom(map.getView().getZoom()-1);
    });
}

function sideLayoutReduceSetting() {
    document.querySelector(".side .reduce").addEventListener('click', function() {
        if(document.querySelector(".side .reduce").classList.contains('on')) {
            document.querySelector(".side .reduce").classList.remove('on');
            $(".side > ul:first-child").slideUp();
            document.querySelector(".side .reduce > span").innerText = '펼치기';
        } else {
            document.querySelector(".side .reduce").classList.add('on');
            $(".side > ul:first-child").slideDown();
            document.querySelector(".side .reduce > span").innerText = '접기';
        }
    });
}