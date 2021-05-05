import moment from 'moment'
import _ from 'lodash'
import numeral from 'numeral'

import globalFnDate from './globalFnDate'

export default { moment, _, numeral, $: globalFnDate.getFns() }
