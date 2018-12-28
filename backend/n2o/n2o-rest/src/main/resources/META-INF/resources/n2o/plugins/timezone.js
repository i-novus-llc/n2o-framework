define([
    'underscore',
    'jquery',
    'n2o/tools/class',
    'infect'
], function (_, $, Class, infect) {
    "use strict";

    var timezone;
    return Class.extend({

        constructor: function () {
            this.timezone = infect.get("application").options.timezone || moment.tz.guess();
        },
        set: function (value) {
            this.timezone = value;
        },
        get: function () {
            return this.timezone;
        }
    });

});