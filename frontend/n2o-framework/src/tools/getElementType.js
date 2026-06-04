/**
 * Возвращает тип createElement() на основе props компонента.
 * Полезно для расчета того, какой тип компонента должен отображаться.
 *
 * @param {function} Component компонент React.
 * @param {object} props атрибуты компонента
 * @returns {string|function} ReactElement
 */
export function getElementType(Component, props) {
    const { defaultProps = {} } = Component

    if (props.as) { return props.as }

    return defaultProps.as || 'div'
}

export default getElementType
