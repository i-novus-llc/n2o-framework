package net.n2oapp.framework.engine.modules.stack;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.processing.DataProcessing;
import net.n2oapp.framework.api.ui.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Тестирование обработки ошибок в {@link DataProcessingStack}
 */
class DataProcessingStackTest {

    @Test
    void testQueryErrorPassedToNextModule() {
        N2oException original = new N2oException("original");
        N2oException replaced = new N2oException("replaced");
        List<N2oException> received = new ArrayList<>();

        DataProcessingStack stack = stackOf(
                new DataProcessing() {
                    @Override
                    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
                        received.add(exception);
                        throw replaced;
                    }
                },
                new DataProcessing() {
                    @Override
                    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
                        received.add(exception);
                        DataProcessing.super.processQueryError(requestInfo, responseInfo, exception);
                    }
                });

        N2oException thrown = assertThrows(N2oException.class,
                () -> stack.processQueryError(new QueryRequestInfo(), new QueryResponseInfo(), original));

        assertThat(received, contains(sameInstance(original), sameInstance(replaced)));
        assertThat(thrown, sameInstance(replaced));
    }

    @Test
    void testActionErrorPassedToNextModule() {
        N2oException original = new N2oException("original");
        List<N2oException> received = new ArrayList<>();

        DataProcessingStack stack = stackOf(
                new DataProcessing() {
                },
                new DataProcessing() {
                    @Override
                    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo,
                                                   DataSet dataSet, N2oException exception) {
                        received.add(exception);
                        DataProcessing.super.processActionError(requestInfo, responseInfo, dataSet, exception);
                    }
                });

        N2oException thrown = assertThrows(N2oException.class,
                () -> stack.processActionError(new ActionRequestInfo<>(), new ActionResponseInfo(), new DataSet(), original));

        assertThat(received, contains(sameInstance(original)));
        assertThat(thrown, sameInstance(original));
    }

    @Test
    void testActionErrorReplacedAndPassedToNextModule() {
        N2oException original = new N2oException("original");
        N2oException replaced = new N2oException("replaced");
        List<N2oException> received = new ArrayList<>();

        DataProcessingStack stack = stackOf(
                new DataProcessing() {
                    @Override
                    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo,
                                                   DataSet dataSet, N2oException exception) {
                        received.add(exception);
                        throw replaced;
                    }
                },
                new DataProcessing() {
                    @Override
                    public void processActionError(ActionRequestInfo<DataSet> requestInfo, ActionResponseInfo responseInfo,
                                                   DataSet dataSet, N2oException exception) {
                        received.add(exception);
                        DataProcessing.super.processActionError(requestInfo, responseInfo, dataSet, exception);
                    }
                });

        N2oException thrown = assertThrows(N2oException.class,
                () -> stack.processActionError(new ActionRequestInfo<>(), new ActionResponseInfo(), new DataSet(), original));

        assertThat(received, contains(sameInstance(original), sameInstance(replaced)));
        assertThat(thrown, sameInstance(replaced));
    }

    @Test
    void testErrorRethrownWhenModuleDoesNotThrow() {
        N2oException original = new N2oException("original");
        N2oException replaced = new N2oException("replaced");
        List<N2oException> received = new ArrayList<>();

        DataProcessingStack stack = stackOf(
                new DataProcessing() {
                    @Override
                    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
                        received.add(exception);
                    }
                },
                new DataProcessing() {
                    @Override
                    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
                        received.add(replaced);
                        throw replaced;
                    }
                });

        assertDoesNotThrow(() -> stack.processQueryError(new QueryRequestInfo(), new QueryResponseInfo(), original));

        assertThat(received, contains(sameInstance(original)));
    }

    @Test
    void testErrorRethrownWhenNoEnabledModules() {
        N2oException original = new N2oException("original");
        List<N2oException> received = new ArrayList<>();

        DataProcessingStack stack = stackOf(
                new DataProcessing() {
                    @Override
                    public void processQueryError(QueryRequestInfo requestInfo, QueryResponseInfo responseInfo, N2oException exception) {
                        received.add(exception);
                    }

                    @Override
                    public boolean isEnabled(RequestInfo requestInfo) {
                        return false;
                    }
                });

        N2oException thrown = assertThrows(N2oException.class,
                () -> stack.processQueryError(new QueryRequestInfo(), new QueryResponseInfo(), original));

        assertThat(received, empty());
        assertThat(thrown, sameInstance(original));
    }

    private static DataProcessingStack stackOf(DataProcessing... modules) {
        return new DataProcessingStack() {
            @Override
            protected List<DataProcessing> findModules() {
                return List.of(modules);
            }
        };
    }
}
