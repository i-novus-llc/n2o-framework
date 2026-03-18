import { ButtonState } from './Toolbar'

// eslint-disable-next-line @typescript-eslint/no-extraneous-class
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
    static get defaultState(): ButtonState {
        return ({
            isInit: false,
            visible: true,
            disabled: false,
            loading: false,
            operations: [],
            key: '',
            buttonId: 'string',
        })
    }
}

export default ButtonResolver
