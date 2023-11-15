// eslint-disable-next-line @typescript-eslint/no-extraneous-class
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
            visibleState: true,
            disabled: false,
            frozen: false,
            key: '',
            columnId: '',
        })
    }
}

export default ColumnsResolver
