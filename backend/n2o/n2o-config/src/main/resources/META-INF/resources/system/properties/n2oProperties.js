//noinspection JSUnresolvedFunction
define([], function() {
    return {
        deleteEnv: function() {
            this.getWidget().editModel().set('envValue', null);
            this.getWidget().editModel().set('hasEnvValue', false);
        },

        addEnv: function() {
            this.getWidget().editModel().set('hasEnvValue', true);
        },

        deleteServlet: function() {
            this.getWidget().editModel().set('servletValue', null);
            this.getWidget().editModel().set('hasServletValue', false);
        },

        addServlet: function() {
            this.getWidget().editModel().set('hasServletValue', true);
        }
    }
});