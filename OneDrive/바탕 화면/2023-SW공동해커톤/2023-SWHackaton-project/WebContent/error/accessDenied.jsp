
<!doctype html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<html lang="ko">
<head>
    
    <app:layout mode="stylescript" type="error"/>

    <style>
        .headerH {height:0px !important;}
		.error {background:#f5f5f5; height:100%;margin:0 auto;}
		.error .error-wrap {padding:100px 0; width:800px; margin:0 auto;text-align:center;}
		.error .error-wrap h2 {color:#ff8080; font-size:34px; font-weight:300; padding-bottom:20px; letter-spacing:2px;font-weight: bold;}
		.error .error-inner {width:800px; display:inline-block; background:#fff; padding:60px 0 60px 0; overflow:hidden;}
		.error .error-inner div {overflow:hidden; text-align:center;}
		.error .error-inner p {color:#696969;}
		.error .error-inner .error_text {}
		.error .error-link {padding-top:10px; text-align:right; width:800px; margin:0 auto;}
		.error .error-link a {display:inline-block; color:#a9a9d3; font-size:16px; font-weight:500; }
		.error .error-link a:hover {border-bottom:2px solid #e2e2f1;}
		.error .error-inner .error_msg {background:#f3f3f3; padding:50px 0;}        
    </style>
</head>

<body>

<body !style="background:#f5f5f5;">
    <header style="background:#ffffff;">
        <div class="head">
         
        </div>
    </header>
    <div class="headerH"></div>

    <section class="error">
        <div class="error-wrap">
            <h2>Access Denied !!</h2>
            <div class="error-inner">
                <div>
                    <p class="error_text">해당 화면에 접근할수있는 권한이 없습니다. 관리자에게 문의 바랍니다. </p>
                </div>
            </div>
            <div class="error-link">
                <a href="javascript:history.back();" title="이전페이지로 이동">이전페이지로 이동</a>
            </div>
        </div>
    </section>

</body>
</html>

