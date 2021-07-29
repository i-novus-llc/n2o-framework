class FormPlugin {
    /**
     * Значение по умолчанию
     * @return {{
     *  filter: *[],
     *  visible: boolean,
     *  visible_field: boolean,
     *  visible_set: boolean,
     *  dependency: null,
     *  disabled: boolean,
     *  disabled_field: boolean,
     *  disabled_set: boolean,
     *  message: null,
     *  loading: boolean,
     *  isInit: boolean,
     *  required: boolean
     * }}
     */
    static get defaultState() {
        return ({
            isInit: true,
            visible: true,
            visible_field: true,
            visible_set: true,
            disabled: false,
            disabled_field: false,
            disabled_set: false,
            message: null,
            filter: [],
            dependency: null,
            required: false,
            loading: false,
        })
    }
}

export default FormPlugin
