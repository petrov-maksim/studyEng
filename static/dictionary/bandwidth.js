function test() {
    async function addWordSetsPicture(file) {
        // let base64ImageData = "/9j/4AAQSkZJRgABAQEAYABgAAD/2wCEAA0JCgs";
        // let formData = reduceImgTransferBandwidth(base64ImageData);
    }
    function reduceImgTransferBandwidth(pimagedata) {
        let form = document.createElement("myForm");
        let ImageURL = "data:image/gif;base64," + pimagedata;
        let block = ImageURL.split(";");
        let contentType = block[0].split(":")[1];
        let realData = block[1].split(",")[1];
        let blob = b64toBlobTest(realData, contentType);
        let formDataToUpload = new FormData(form);

        formDataToUpload.append("image", blob);

        return formDataToUpload;
    }

    function b64toBlobTest(b64Data, contentType, sliceSize) {
        contentType = contentType || '';
        sliceSize = sliceSize || 512;

        let byteCharacters = atob(b64Data);
        let byteArrays = [];

        for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
            let slice = byteCharacters.slice(offset, offset + sliceSize);

            let byteNumbers = new Array(slice.length);
            for (let i = 0; i < slice.length; i++) {
                byteNumbers[i] = slice.charCodeAt(i);
            }

            let byteArray = new Uint8Array(byteNumbers);

            byteArrays.push(byteArray);
        }

        return new Blob(byteArrays, {
            type : contentType
        });
    }
}