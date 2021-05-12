import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
/**
 * Утилита позволяющая подписываться на изменения хранилища через селектор. В случае изменения вызывается callback
 * @param {Object} store - хранилище Redux
 * @param {Function} select - селектор возвращающий нужное значение
 * @param {Function} onChange - callback функция вызывающаяся при изменение данных
 * @example
 * class MyObserveExample extends React.Component {
 * ...
 *  componentDidMount() {
 *    observeStore(this.context.store, (state) => state.dataList, (state) =>
 *      this.setState({
 *        data: state
 *      })
 *    );
 *  }
 * ...
 * }
 *
 * MyObserveExample.contextTypes = {
 *  store: PropTypes.object,
 * };
 */
export default function observeStore(store, select, onChange) {
    let currentState

    function handleChange() {
        const nextState = select(store.getState())

        if (!isEqual(nextState, currentState) && !isEmpty(nextState)) {
            currentState = nextState
            onChange(currentState)
        }
    }

    const unsubscribe = store.subscribe(handleChange)

    handleChange()

    return unsubscribe
}
