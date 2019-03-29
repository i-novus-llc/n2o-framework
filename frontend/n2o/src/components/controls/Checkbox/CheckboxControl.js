import { withHandlers } from 'recompose';
import CheckboxN2O from './CheckboxN2O';

export default withHandlers({
  onChange: props => event => {
    props.onChange(event.nativeEvent.target.checked);
  }
})(CheckboxN2O);
