<%@ page import="Storage.SessioneDiValidazione.SessioneDiValidazione" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <jsp:include page="/WEB-INF/Interface/Partials/Head.jsp">
        <jsp:param name="docenteStyles" value="docente,grid-layoutCSS,sessione"/>
        <jsp:param name="moduloAIStyles" value="seatchart"/>
        <jsp:param name="docenteScripts" value="docente,elencoEsiti"/>
        <jsp:param name="title" value="Easy Pass | Docente"/>
    </jsp:include>
    <script src="${pageContext.request.contextPath}/js/jsQR.js"></script>
</head>
<body>
<div class="coll-2">
    <div class="center">
        <a class="btnn" id="logout" href="javascript:void(0)">LOGOUT</a>
    </div>
    <%  SessioneDiValidazione sessioneDiValidazione = (SessioneDiValidazione) session.getAttribute("sessioneDiValidazione");
        List<Integer> solutionGA = (List<Integer>) session.getAttribute("seatingMap");
        String[] roomSize = (String[]) session.getAttribute("roomSize");
        String path = sessioneDiValidazione.getQRCode();
    %>
    <div class="room_QR">
        <img src='http://localhost:8080/Progetto_EasyPass/QRCodes/<%=path%>' alt="QRcode" id="qrcode" class="qrCode">
        <div class="content">
            <div id="map-container"></div>
        </div>
    </div>
    <span id="linkQRcode"></span>
    <canvas id="canvas" hidden></canvas>
    <hr class="rounded">
    <div class="grid-container" id="elencoEsitiDiv">
        <c:forEach var="esito" items="${esiti}">
            <div class="grid-cell shadow">
                <span class="grid-data" name="nomeCompleto" id="nomeCompleto">${esito.nomeStudente} ${esito.cognomeStudente}</span>
                <span class="grid-data" name="ddn" id="ddn">${esito.dataDiNascitaStudente}</span>
                <span class="grid-data" name="esito" id="esito">${esito.validita}</span>
            </div>
        </c:forEach>
    </div>
    <div style="display: flex; justify-content: space-between">
        <span class="esitoCounter" id="esitoCounter">0/${param.nStudents}</span>
        <button onclick="startAnteprimaReport()" class="termina-sessione-button">Termina</button>
    </div>
</div>
<%@include file="/WEB-INF/Interface/Partials/Logout.jsp"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/ModuloAI/seatchart.js"></script>
<script>
    window.addEventListener('load', function () {
        var canvasElement = document.getElementById("canvas");
        var canvas = canvasElement.getContext("2d");

        var img = document.getElementById('qrcode');
        var imageData;

        if (img.getAttribute('src') === "") {
            alert("Nessun Immagine Trovata");
            return;
        }
        canvasElement.height = img.height;
        canvasElement.width = img.width;
        canvas.drawImage(img, 0, 0, canvasElement.width, canvasElement.height);
        imageData = canvas.getImageData(0, 0, canvasElement.width, canvasElement.height);

        const code = jsQR(imageData.data, imageData.width, imageData.height);
        if (code) {
            let urlQRcode = document.getElementById("linkQRcode");

            while( urlQRcode.firstChild ) {
                urlQRcode.removeChild( urlQRcode.firstChild );
            }
            urlQRcode.appendChild( document.createTextNode(code.data));
            URL.revokeObjectURL(img.src);
        } else {
            URL.revokeObjectURL(img.src);
            alert("Nessun QR trovato")
        }
    })

    const url_string = window.location.href;
    const url = new URL(url_string);
    const paramValue = url.searchParams.get("nStudents");
    var setInterval = setInterval(function (){
        ajaxUpdate(paramValue,setInterval)}, 1000);


    /** Mappa Studenti */
    const options = {
        map: {
            id: 'map-container',
            rows: <%=Integer.parseInt(roomSize[0])%>,
            columns: <%=Integer.parseInt(roomSize[1])%>,
            // e.g. Reserved Seat [Row: 1, Col: 2] = 7 * 1 + 2 = 9
            reserved: {
                seats:[],
            },
            disabled: {
                //seats: [7],
                //rows: [3],
                //columns: [7]
            }
        },
        types: [
            { type: "reduced", backgroundColor: "#287233", price: 7.5, selected: <%=solutionGA%> }
        ],
    };
    const sc = new Seatchart(options);
</script>
</body>
</html>