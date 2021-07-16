class ButtonResolver {
    /**
     * Значение поумолчанию
     * @return {{
     *  visible: boolean,
     *  color: string | null,
     *  count: number,
     *  icon: string | null,
     *  title: string | null,
     *  message: any | null,
     *  loading: boolean,
     *  error: any | null,
     *  size: string | null,
     *  hint: string | null,
     *  disabled: boolean,
     *  conditions: Object | null,
     *  isInit: boolean
     * }}
     */
    static get defaultState() {
        return ({
            isInit: true,
            visible: true,
            count: 0,
            size: null,
            color: null,
            title: null,
            hint: null,
            message: null,
            icon: null,
            disabled: false,
            loading: false,
            error: null,
            conditions: null,
        })
    }
}

export default ButtonResolver
