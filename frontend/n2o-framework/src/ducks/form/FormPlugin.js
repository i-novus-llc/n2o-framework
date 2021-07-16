class FormPlugin {
    /**
     * Значение по умолчанию
     * @return {{
     *  filter: *[],
     *  visible: boolean,
     *  dependency: null,
     *  disabled: boolean,
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
            disabled: false,
            message: null,
            filter: [],
            dependency: null,
            required: false,
            loading: false,
        })
    }
}

export default FormPlugin
