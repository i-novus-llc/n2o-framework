var $ = function() {

    var FORMAT = "yyyy-MM-ddTHH:mm:ss";
    var FORMAT00 = "yyyy-MM-ddT00:00:00";

    return {

        now : function() {
            return moment().format(FORMAT);
        },

        today : function() {
            return moment().format(FORMAT00);
        },


        beginWeek : function() {
            return moment().startOf('isoWeek').format(FORMAT00);
        },

        endWeek : function() {
            return moment().endOf('isoWeek').format(FORMAT00);
        },

        beginMonth : function() {
            return moment().startOf('month').format(FORMAT00);
        },

        endMonth : function() {
            return moment().endOf('month').format(FORMAT00);
        },

        beginQuarter : function() {
            return moment().startOf('quarter').format(FORMAT00);
        },

        endQuarter : function() {
            return moment().endOf('quarter').format(FORMAT00);
        },

        beginYear : function() {
            return moment().startOf('year').format(FORMAT00);
        },

        endYear : function() {
            return moment().endOf('year').format(FORMAT00);
        }
    }
}();


