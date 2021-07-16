class PageResolver {
    /**
     * Значение поумолчанию
     * @return {{
     *  metadata: {},
     *  disabled: boolean,
     *  loading: boolean,
     *  error: boolean,
     *  status: null
     * }}
     */
    static get defaultState() {
        return ({
            metadata: {},
            loading: false,
            error: false,
            disabled: false,
            status: null,
        })
    }
}

export default PageResolver
