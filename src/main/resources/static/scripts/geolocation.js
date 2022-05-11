if (navigator.geolocation) {
    navigator.geolocation.watchPosition(function (position) {
        document.getElementById("longitude").value = position.coords.longitude;
        document.getElementById("latitude").value = position.coords.latitude;
    });
} else {
    document.getElementById("longitude").value = null;
    document.getElementById("latitude").value = null;
}
