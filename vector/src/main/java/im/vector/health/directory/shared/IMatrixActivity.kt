package im.vector.health.directory.shared

import org.matrix.androidsdk.MXSession

interface IMatrixActivity: IProgressBarPresenter, IFragmentActivity {
    var currentSession: MXSession
}