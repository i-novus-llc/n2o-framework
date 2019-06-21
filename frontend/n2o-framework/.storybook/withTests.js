import { withTests } from '@storybook/addon-jest';
import jestTestResuls from './jest-test-results.json';

export default withTests({results: jestTestResuls});