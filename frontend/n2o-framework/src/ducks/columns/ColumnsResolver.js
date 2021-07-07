class ColumnsResolver {
    /**
     * Значение по умолчанию
     * @return {{
     *  visible: boolean,
     *  frozen: boolean,
     *  disabled: boolean,
     *  isInit: boolean
     * }}
     */
    static get defaultState() {
        return ({
            isInit: true,
            visible: true,
            disabled: false,
            frozen: false,
        })
    }
}

export default ColumnsResolver
