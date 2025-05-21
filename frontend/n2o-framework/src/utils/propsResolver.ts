import { Resolve, propsResolver as resolver } from '../core/Expression/propsResolver'

import warning from './warning'
import { DEFAULT_CONTEXT } from './evalExpression'

warning(true, '"./utils/propsResolver" is deprecated, use "./core/Expression/useResolver"')

/**
 * Функция преобразует шаблоные props свойства вида \`name\` в константные данные из контекста
 * @param {Object} props - объект свойств которые требуется преобразовать
 * @param {Object} model - объект контекста, над которым будет произведенно преобразование
 * @param {Array} ignoreKeys - дополнительные исключения
 * @return {Object}
 * @example
 * const props = {
 *  fio: "`surname+' '+name+' '+middleName`"
 * }
 *
 * const model = {
 *  surname: "Иванов",
 *  name: "Иван",
 *  middleName: "Иванович",
 * }
 *
 * console.log(propsResolver(props, model))
 *
 * //- {fio: "Иванов Иван Иванович"}
 * @deprecated
 */
export default function propsResolver<
    Resolved extends Resolve<Prop>,
    Prop = unknown,
>(
    props: Prop,
    model: Record<string, unknown> | Array<Record<string, unknown>> = {},
    ignoreKeys: string[] = [],
) {
    return resolver<Resolved>(props, model, DEFAULT_CONTEXT, ignoreKeys)
}
