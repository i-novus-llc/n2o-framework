class WidgetResolver {
    static get defaultState() {
        return ({
            isInit: false,
            isVisible: true,
            visible: true,
            fetch: 'always',
            disabled: false,
            isResolved: false,
            isFilterVisible: true,
            isActive: false,
            type: null,
            /* Query props */
            /* System props */
            pageId: null,
            error: null,
            validation: {},
            // datasource: null,
        })
    }
}

export default WidgetResolver
