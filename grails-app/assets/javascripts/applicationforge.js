var getJSON = function(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', url, true);
    xhr.responseType = 'json';
    xhr.onload = function() {
        var status = xhr.status;
        if (status == 200) {
            callback(null, xhr.response);
        } else {
            callback(status);
        }
    };
    xhr.send();
};

// Returns a function, that, as long as it continues to be invoked, will not
// be triggered. The function will be called after it stops being called for
// N milliseconds. If `immediate` is passed, trigger the function on the
// leading edge, instead of the trailing.
function debounce(func, wait, immediate) {
    var timeout;
    return function() {
        var context = this, args = arguments;
        var later = function() {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        var callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
};

function onFeatureChange() {
    var version = getSelectValue("version");
    var projectType = getSelectValue("projectType");
    var profile = getSelectValue("profile");
    var name = document.getElementById('name').value;
    var features = getCheckedBoxesValue("features");
    var urlParams = 'version='+version+'&projectType='+projectType+'&profile='+profile+'&name='+name+'&features='+features;
    refreshUi(urlParams);
}

function onProjectTypeChange() {
    var projectType = getSelectValue("projectType");
    var name = document.getElementById('name').value;
    var urlParams = 'projectType='+projectType+'&name='+name;
    refreshUi(urlParams);
}

function onVersionChange() {
    var version = getSelectValue("version");
    var projectType = getSelectValue("projectType");
    var name = document.getElementById('name').value;
    var urlParams = 'version='+version+'&projectType='+projectType+'&name='+name;
    refreshUi(urlParams);
}

function onNameChange() {
    var version = getSelectValue("version");
    var projectType = getSelectValue("projectType");
    var profile = getSelectValue("profile");
    var name = document.getElementById('name').value;
    var features = getCheckedBoxesValue("features");
    var urlParams = 'version='+version+'&projectType='+projectType+'&profile='+profile+'&name='+name+'&features='+features;
    refreshUi(urlParams);
}

function onSelectChange() {
    var version = getSelectValue("version");
    var projectType = getSelectValue("projectType");
    var profile = getSelectValue("profile");
    var name = document.getElementById('name').value;
    var urlParams = 'version='+version+'&projectType='+projectType+'&profile='+profile+'&name='+name;
    refreshUi(urlParams);
}

function refreshUi(urlParams) {
    document.getElementById('loading').style.visibility = "visible";
    getJSON('projectoptions?'+urlParams, function(err, data) {
        if (err != null) {
            console.log('Something went wrong');

        } else {
            if ( data.length == 0 ) {
                console.log('No data returned');

            } else {
                document.getElementById('curlCommand').innerText = data.curlCommand;

                var html = generateHtmlOptions(data.projectOptions.projectTypes, data.projectOptions.selectedProjectType);
                document.getElementById('projectType').innerHTML = html;

                html = generateHtmlOptions(data.projectOptions.versions, data.projectOptions.selectedVersion);
                document.getElementById('version').innerHTML = html;

                html = generateHtmlOptions(data.projectOptions.profiles, data.projectOptions.selectedProfile);
                document.getElementById('profile').innerHTML = html;

                html = generateHtmlCheckbox(data.projectOptions.features, data.projectOptions.selectedFeatures, data.projectOptions.requiredSelectedFeatures);
                document.getElementById('featureList').innerHTML = html;

                document.getElementById('name').value = data.projectOptions.name;
            }
        }
        document.getElementById('loading').style.visibility = "hidden";
    });
}

function generateHtmlCheckbox(features, selectedFeatures, requiredSelectedFeatures) {

    features.sort();
    var html = '';
    for (var i = 0; i < features.length; i++ ) {
        var feature  = features[i];
        html += "<li>";

        var isSelectedFeature = false;
        for (var y = 0; y < selectedFeatures.length; y++ ) {
            var defaultSelectedFeature = selectedFeatures[y];
            if (defaultSelectedFeature.toLowerCase() == feature.toLowerCase() ) {
                isSelectedFeature = true;
                break;
            }
        }
        if ( isSelectedFeature ) {
            html += "<input name='features' onclick='onFeatureChange()' type='checkbox' checked='checked' value='"+feature+"'/>";
        }
        var isRequiredFeature = false;
        for (var z = 0; z < requiredSelectedFeatures.length; z++ ) {
            var requiredSelectedFeature = requiredSelectedFeatures[z];
            if (requiredSelectedFeature.toLowerCase() == feature.toLowerCase() ) {
                isRequiredFeature = true;
                break;
            }
        }
        if ( isRequiredFeature ) {
            html += "<input name='features' onclick='onFeatureChange()' type='checkbox' checked='checked' disabled='disabled' value='"+feature+"'/>";
        }

        if ( !isSelectedFeature && !isRequiredFeature )  {
            html += "<input name='features' onclick='onFeatureChange()' type='checkbox' value='"+feature+"'/>";
        }
        html += "<span>"+feature+"</span>";
        html += "</li>";
    }
    return html;
}

function generateHtmlOptions(values, value) {
    var html = '';
    for (var i = 0; i < values.length; i++ ) {
        var v = values[i];
        if ( v === value ) {
            html += '<option value="'+v+'" selected="selected">'+v+'</option>'
        } else {
            html += '<option value="'+v+'">'+v+'</option>'
        }
    }
    return html;
}

function getSelectValue(selectId) {
    return document.getElementById(selectId).value;
}

function getCheckedBoxesValue(chkboxName) {
    var checkboxes = document.getElementsByName(chkboxName);
    var checkboxesChecked = [];
    for (var i=0; i<checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            checkboxesChecked.push(checkboxes[i].value);
        }
    }
    return checkboxesChecked.length > 0 ? checkboxesChecked : null;
}

var nameInputListener = debounce(function() {
    onNameChange();
}, 500);

function onNameChanged() {
    nameInputListener();
}