package distudios.at.carcassonne.gui.field;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import distudios.at.carcassonne.CarcassonneApp;
import distudios.at.carcassonne.R;
import distudios.at.carcassonne.engine.logic.Card;
import distudios.at.carcassonne.engine.logic.GameState;
import distudios.at.carcassonne.engine.logic.IGameController;
import distudios.at.carcassonne.engine.logic.Orientation;
import distudios.at.carcassonne.networking.INetworkController;
import distudios.at.carcassonne.networking.connection.CarcassonneMessage;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Button buttonEndTurn;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayfieldView playfieldView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_game, container, false);

        playfieldView = view.findViewById(R.id.view_playfield);
        Button buttonAction = view.findViewById(R.id.button_doSomething);
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IGameController controller = CarcassonneApp.getGameController();
                controller.drawCard();
                controller.removeFromStack(controller.getCurrentCard());
                playfieldView.addPossibleLocations();
            }
        });

        Button buttonCenter = view.findViewById(R.id.button_centerField);
        buttonCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playfieldView.centerCard(null);
//
//
//                // test method -> just send new gamestate to other device
//                CarcassonneApp.getGameController().updateGameState();

                IGameController controller = CarcassonneApp.getGameController();
                Card current = controller.getCurrentCard();
                if (current != null && !controller.hasPlacedCard()) {
                    Orientation o =current.getOrientation();
                    int next = (o.getValue() + 1) % 4;
                    Toast.makeText(getContext(), "Orientation " + current.getOrientation() + " " + Orientation.valueOf(next), Toast.LENGTH_SHORT).show();
                    current.setOrientation(Orientation.valueOf(next));
                    playfieldView.addPossibleLocations();
                }
            }
        });

        buttonEndTurn = view.findViewById(R.id.button_endTurn);
        buttonEndTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IGameController controller = CarcassonneApp.getGameController();
                Log.d("TURN", controller.getGameState().currentPlayer + "" );
                controller.endTurn();
                Log.d("TURN", controller.getGameState().currentPlayer + "");
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void updatePlayField() {
        playfieldView.initFieldFromGameState();
//        IGameController controller = CarcassonneApp.getGameController();
//        if (!controller.isMyTurn()) {
//            buttonEndTurn.setEnabled(false);
//        } else {
//            if (controller.hasPlacedCard()) {
//                buttonEndTurn.setEnabled(true);
//            } else {
//                buttonEndTurn.setEnabled(false);
//            }
//        }

    }
}
